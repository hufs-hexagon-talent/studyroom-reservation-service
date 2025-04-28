package hufs.computer.studyroom.common.util.excel.annotation;


import hufs.computer.studyroom.common.util.excel.style.ExcelCellStyle;
import hufs.computer.studyroom.common.util.excel.style.body.DefaultBodyStyle;
import hufs.computer.studyroom.common.util.excel.style.header.DefaultHeaderStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * DTO 필드를 엑셀 컬럼에 매핑하기 위한 어노테이션
 * - headerName: 컬럼 제목 지정
 * - headerStyle: 헤더 셀 스타일 전략 지정
 * - bodyStyle: 바디 셀 스타일 전략 지정
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String headerName() default "";
    Class<? extends ExcelCellStyle> headerStyle() default DefaultHeaderStyle.class;
    Class<? extends ExcelCellStyle> bodyStyle()   default DefaultBodyStyle.class;
}