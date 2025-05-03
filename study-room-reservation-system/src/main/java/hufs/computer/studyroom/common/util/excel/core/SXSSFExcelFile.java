package hufs.computer.studyroom.common.util.excel.core;

import hufs.computer.studyroom.common.error.code.ExcelErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.excel.meta.ExcelMetaDataFactory;
import hufs.computer.studyroom.common.util.excel.meta.ExcelRenderLocation;
import hufs.computer.studyroom.common.util.excel.meta.ExcelRenderResource;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import static hufs.computer.studyroom.common.util.reflection.SuperClassReflectionUtils.getField;


/*
 * 대용량 엑셀 처리용 추상 클래스(SXSSF 기반)
 * - Workbook 초기화 및 @ExcelColumn 메타 데이터 스캔
 * - 공통 렌더링 메서드 제공(renderHeaders, renderBody)
 * - write() 호출 시 리소스(임시 파일) 정리 포함
 *
                     ExcelFile - 인터페이스
                               ↑
                 SXSSFExcelFile - 추상 클래스
                               ↑
            OneSheetExcelFile, MultiSheetExcelFile  - 구현 클래스

            메타 데이터 → 렌더링 전용 객체
*/
// ExcelFile 의 구현체 이자 추상 클래스 <- 단일 시트와 멀티 시트의 추상 클래스
public abstract class SXSSFExcelFile<T> implements ExcelFile {

    protected static final SpreadsheetVersion SUPPLY_EXCEL_VERSION = SpreadsheetVersion.EXCEL2007; // 1,048,576 rows
    protected static final int ROW_START_INDEX = 0;
    protected static final int COLUMN_START_INDEX = 0;
    protected static final int CELL_PADDING_SIZE = 8;
    protected static final int EXCEL_COLUMN_UNIT = 256; // POI에서 컬럼 단위(1글자 = 256)

    protected final SXSSFWorkbook workbook;
    protected final ExcelRenderResource resource; // 헤더명·필드목록·셀스타일 보유
    protected Sheet sheet;                        // 현재 작업 중인 Sheet

    /* ====== 생성 ====== */
    public SXSSFExcelFile(List<T> data, Class<T> dtoType) {

        validateMaxRow(data);

        this.workbook = new SXSSFWorkbook();
        this.resource = ExcelMetaDataFactory.create(dtoType, workbook);

        renderExcel(data);  // 템플릿 메서드 → 구현체가 시트 분할 전략 결정
    }

    // 행 수 검증
    protected void validateMaxRow(List<T> data) {
        int maxRows = SUPPLY_EXCEL_VERSION.getMaxRows();
        if (data.size() > maxRows) {
            throw new CustomException(ExcelErrorCode.ROW_LIMIT_EXCEEDED);
        }
    }

    // 구현체에서 정의할 빈칸 메서드(훅): 부모가 틀만 만들고 자식이 내용 채우는 확장 지점
    protected abstract void renderExcel(List<T> data);

    // 헤더 한 줄 그리기
    protected void renderHeaders(Sheet sheet, int rowIdx, int colStartIdx) {
        Row row = sheet.createRow(rowIdx);
        int colIdx = colStartIdx;

        for (String fieldName : resource.getDataFieldNames()) {
            Cell cell = row.createCell(colIdx++);
            cell.setCellValue(resource.getExcelHeaderName(fieldName));
            cell.setCellStyle(resource.getCellStyle(fieldName, ExcelRenderLocation.HEADER));
        }
    }

    // DTO 한 건 → 바디 한 줄
    protected void renderBody(Object dto, int rowIdx, int colStartIdx) {
        Row row = sheet.createRow(rowIdx);
        int colIdx = colStartIdx;

        for (String fieldName : resource.getDataFieldNames()) {
            Cell cell = row.createCell(colIdx++);
            try {
                Field field = getField(dto.getClass(), fieldName); field.setAccessible(true);
                renderCellValue(cell, field.get(dto));

                cell.setCellStyle(resource.getCellStyle(fieldName, ExcelRenderLocation.BODY));

            } catch (Exception e) {
                throw new CustomException(ExcelErrorCode.RENDER_ERROR);
            }
        }
    }

    // Primitive(원시)객체 → Cell 값 세팅
    private void renderCellValue(Cell cell, Object value) {
        if (value instanceof Number num) {
            cell.setCellValue(num.doubleValue());
        } else {
            cell.setCellValue(value == null ? "" : value.toString());
        }
    }

    @Override
    public void write(OutputStream out) throws IOException {
        try (out; workbook) {
            workbook.write(out);
        } finally {
            workbook.dispose();          // temp 파일 정리
        }
    }

    /**
     * 모든 열(0 ~ columnCount-1)을 자동폭 계산에 추적
     *
     * @param columnCount 추적할 열 개수
     */
    protected void trackAllColumns(int columnCount) {
        if (!(sheet instanceof SXSSFSheet sxssfSheet)) {
            return;                 // XSSF/HSSF → 추적 불필요
        }

        for (int colIdx = 0; colIdx < columnCount; colIdx++) {
            sxssfSheet.trackColumnForAutoSizing(colIdx);
        }
    }

    /**
     * 자동폭(autoSizeColumn) 계산 후,
     * paddingChars 글자 폭만큼 추가 여유 (글자 1칸 = 256 단위)
     *
     * @param paddingChars 추가할 글자 폭(예: 2 → 2*256)
     */
    protected void setAutoSizeAllColumns(int paddingChars) {

        // XSSF/HSSF → 자동폭 미지원
        if (!(sheet instanceof SXSSFSheet sxssfSheet)) return;

        int columnCount = resource.getDataFieldNames().size();

        for (int colIdx = 0; colIdx < columnCount; colIdx++) {

            // 자동 폭 계산
            sxssfSheet.autoSizeColumn(colIdx);
            int autoWidth   = sxssfSheet.getColumnWidth(colIdx);
            int paddedWidth = autoWidth + paddingChars * EXCEL_COLUMN_UNIT;
            sxssfSheet.setColumnWidth(colIdx, paddedWidth);
        }
    }
}
