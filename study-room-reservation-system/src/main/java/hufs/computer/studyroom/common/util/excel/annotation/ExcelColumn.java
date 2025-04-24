package hufs.computer.studyroom.common.util.excel.annotation;

import hufs.computer.studyroom.common.util.excel.style.NoExcelCellStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String headerName() default "";
//    ExcelColumnStyle headerStyle() default @ExcelColumnStyle(excelCellStyleClass = NoExcelCellStyle.class);
//    ExcelColumnStyle bodyStyle() default @ExcelColumnStyle(excelCellStyleClass = NoExcelCellStyle.class);

}


/*
*
  @ExcelColumn(
        headerName = "회사",
        headerStyle = @ExcelColumnStyle(excelCellStyleClass = GreyHeaderStyle.class),
        bodyStyle = @ExcelColumnStyle(excelCellStyleClass = BodyStyle.class)
  )
  private final String company;


  @ExcelColumn(
        headerName = "차종",
        headerStyle = @ExcelColumnStyle(excelCellStyleClass = GreyHeaderStyle.class),
        bodyStyle = @ExcelColumnStyle(excelCellStyleClass = BodyStyle.class)
  )
  private final String name;
*
*
*
* | 회사 | 차종 |
* -------------
* |     |     |
* |     |     |
* |     |     |
* |     |     |
* */
