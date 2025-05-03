package hufs.computer.studyroom.common.util.excel.style.header;

import hufs.computer.studyroom.common.util.excel.style.CustomExcelCellStyle;

import hufs.computer.studyroom.common.util.excel.style.ExcelAlign;
import hufs.computer.studyroom.common.util.excel.style.ExcelBorderStyle;
import hufs.computer.studyroom.common.util.excel.style.ExcelCellStyleConfigurer;

public class GrayHeaderStyle extends CustomExcelCellStyle {
    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.backgroundColor(201, 201, 201)
                .allBorders(ExcelBorderStyle.THIN)
                .align(ExcelAlign.CENTER, ExcelAlign.CENTER);
    }
}
