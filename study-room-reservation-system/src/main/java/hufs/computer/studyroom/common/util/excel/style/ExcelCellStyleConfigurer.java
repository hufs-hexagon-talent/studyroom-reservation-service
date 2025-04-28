package hufs.computer.studyroom.common.util.excel.style;


import hufs.computer.studyroom.common.util.excel.style.color.DefaultExcelColor;
import hufs.computer.studyroom.common.util.excel.style.color.ExcelColor;
import hufs.computer.studyroom.common.util.excel.style.color.RgbExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * CellStyle 설정 헬퍼
 *
 * - 배경색, 테두리, 정렬 등을 체이닝 방식으로 구성
 */
public class ExcelCellStyleConfigurer {
    // 색상
    private ExcelColor backgroundColor = new DefaultExcelColor();

    // 테두리
    private ExcelBorderStyle borderStyle = ExcelBorderStyle.NONE;

    // 정렬
    private ExcelAlign horizontalAlign = ExcelAlign.LEFT;
    private ExcelAlign verticalAlign   = ExcelAlign.TOP;


    public ExcelCellStyleConfigurer backgroundColor(int red, int green, int blue) {
         this.backgroundColor = new RgbExcelColor(red, green, blue);
        return this;
    }

    public ExcelCellStyleConfigurer allBorders(ExcelBorderStyle style) {
        this.borderStyle = style;
        return this;
    }

    public ExcelCellStyleConfigurer align(ExcelAlign horizontal, ExcelAlign vertical) {
        this.horizontalAlign = horizontal;
        this.verticalAlign   = vertical;
        return this;
    }


    public void configure(CellStyle cellStyle) {
        backgroundColor.applyBackground(cellStyle);

        cellStyle.setBorderTop(borderStyle.getBorderStyle());
        cellStyle.setBorderBottom(borderStyle.getBorderStyle());
        cellStyle.setBorderLeft(borderStyle.getBorderStyle());
        cellStyle.setBorderRight(borderStyle.getBorderStyle());

        cellStyle.setAlignment(horizontalAlign.getHorizontalAlign());
        cellStyle.setVerticalAlignment(verticalAlign.getVerticalAlign());

    }
}
