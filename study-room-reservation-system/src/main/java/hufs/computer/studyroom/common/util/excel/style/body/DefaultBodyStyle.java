package hufs.computer.studyroom.common.util.excel.style.body;

import hufs.computer.studyroom.common.util.excel.style.CustomExcelCellStyle;
import hufs.computer.studyroom.common.util.excel.style.ExcelAlign;
import hufs.computer.studyroom.common.util.excel.style.ExcelBorderStyle;
import hufs.computer.studyroom.common.util.excel.style.ExcelCellStyleConfigurer;

public class DefaultBodyStyle extends CustomExcelCellStyle {
    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.backgroundColor(255, 255, 255)
                .allBorders(ExcelBorderStyle.THIN)
                .align(ExcelAlign.CENTER, ExcelAlign.CENTER);
        // todo cell w , h
    }
}
