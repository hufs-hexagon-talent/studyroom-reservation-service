package hufs.computer.studyroom.common.util.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * CellStyle 전략 인터페이스
 *
 * - apply(CellStyle): 색·테두리·정렬 등 스타일 설정
 */
public interface ExcelCellStyle {
    void apply(CellStyle cellStyle);   // 색·테두리·정렬 등
}
