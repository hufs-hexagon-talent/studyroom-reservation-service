package hufs.computer.studyroom.common.util.excel.meta;

import hufs.computer.studyroom.common.error.code.ExcelErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.excel.style.ExcelCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * CellStyle 생성 팩토리
 *
 * - 전략 클래스 인스턴스화 후 스타일 적용
 */
public final class ExcelStyleMetaDataFactory {
    private ExcelStyleMetaDataFactory() {}

    public static CellStyle create(SXSSFWorkbook workbook,
                                   Class<? extends ExcelCellStyle> strategyClass) {

        try {
            // 전략 인스턴스 생성
            ExcelCellStyle strategy = strategyClass.getDeclaredConstructor().newInstance();

            // 워크북 으로 부터 CellStyle 생성
            CellStyle cellStyle = workbook.createCellStyle();

            strategy.apply(cellStyle);
            return cellStyle;
        } catch (ReflectiveOperationException e) {
            throw new CustomException(ExcelErrorCode.RENDER_ERROR);
        }
    }
}
