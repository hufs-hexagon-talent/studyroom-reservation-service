package hufs.computer.studyroom.common.util.excel.style.color;

import hufs.computer.studyroom.common.error.code.ExcelErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * RGB 값을 이용한 배경색 전략 구현
 *
 * - 유효 범위 체크 후 색상 설정
 */
public class RgbExcelColor implements ExcelColor {
    private static final int MIN_RGB = 0;
    private static final int MAX_RGB = 255;

    private byte red;
    private byte green;
    private byte blue;

    public RgbExcelColor(int red, int green, int blue) {
        if (isOutOfRange(red, green, blue)) {
            throw new CustomException(ExcelErrorCode.INVALID_RGB_VALUE);
        }
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }


    // Cell의 배경색을 지정하기 위해 사용하는 POI method는 setFillForegroundColor 입니다.
    @Override
    public void applyBackground(CellStyle cellStyle) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
        xssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{red, green, blue}, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private boolean isOutOfRange(int red, int green, int blue) {
        return (red < MIN_RGB || red > MAX_RGB || green < MIN_RGB ||
                green > MAX_RGB || blue < MIN_RGB || blue > MAX_RGB );
    }
}
