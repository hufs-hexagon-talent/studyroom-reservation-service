package hufs.computer.studyroom.common.util.excel.annotation;

import hufs.computer.studyroom.common.util.excel.ExcelCellStyle;

public @interface ExcelColumnStyle {
    Class<? extends ExcelCellStyle> excelCellStyleClass();
}
