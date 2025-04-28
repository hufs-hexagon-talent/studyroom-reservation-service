package hufs.computer.studyroom.common.util.excel.meta;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * ExcelMetaData 및 ExcelStyleMetaData 생성 팩토리
 *
 * - DTO 타입과 Workbook 을 받아 ExcelRenderResource 반환
 */
public final class ExcelMetaDataFactory {
    private ExcelMetaDataFactory() {}

    public static ExcelRenderResource create(Class<?> dtoType, SXSSFWorkbook workbook) {

        ExcelMetaData excelMetaData = new ExcelMetaData(dtoType); // 애노테이션 스캔 -> 메타 데이터 추출

        ExcelStyleMetaData styleMetaData = new ExcelStyleMetaData(dtoType, workbook); // 애노테이션 스캔 -> 메타 데이터 추출

        // 읽기 전용 맵핑
        return new ExcelRenderResource(excelMetaData, styleMetaData);
    }

}
