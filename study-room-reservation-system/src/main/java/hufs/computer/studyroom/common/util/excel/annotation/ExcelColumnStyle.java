package hufs.computer.studyroom.common.util.excel.annotation;


import hufs.computer.studyroom.common.util.excel.style.ExcelCellStyle;

/**
 * 필드별 ExcelCellStyle 전략을 지정하는 어노테이션
 * - value: 적용할 ExcelCellStyle 구현체 클래스
 */
public @interface ExcelColumnStyle {
    Class<? extends ExcelCellStyle> value(); //excelCellStyleClass();
}
