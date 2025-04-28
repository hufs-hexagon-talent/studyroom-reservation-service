package hufs.computer.studyroom.common.util.excel.style.header;

import hufs.computer.studyroom.common.util.excel.style.CustomExcelCellStyle;
import hufs.computer.studyroom.common.util.excel.style.ExcelAlign;
import hufs.computer.studyroom.common.util.excel.style.ExcelBorderStyle;
import hufs.computer.studyroom.common.util.excel.style.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {
    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.backgroundColor(223, 235, 246)
                .allBorders(ExcelBorderStyle.THIN)
                .align(ExcelAlign.CENTER, ExcelAlign.CENTER);
    }
}
