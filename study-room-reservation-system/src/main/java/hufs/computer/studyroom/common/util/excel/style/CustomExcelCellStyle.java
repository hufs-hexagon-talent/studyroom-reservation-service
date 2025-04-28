package hufs.computer.studyroom.common.util.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * 기본 스타일 구성 로직 제공 추상 클래스
 *
 * - configure(configurer) 추상화 메서드 구현 시점에 스타일 설정
 */
public abstract class CustomExcelCellStyle implements ExcelCellStyle {
    private ExcelCellStyleConfigurer configurer = new ExcelCellStyleConfigurer();

    public CustomExcelCellStyle() {
        configure(configurer);
    }

    public abstract void configure(ExcelCellStyleConfigurer configurer);

    @Override
    public void apply(CellStyle cellStyle) {
        configurer.configure(cellStyle);
    }

}
