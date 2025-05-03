package hufs.computer.studyroom.common.util.excel.style.color;

import org.apache.poi.ss.usermodel.CellStyle;

/**
 * 배경색 전략 인터페이스
 *
 * - applyBackground(CellStyle)
 */
public interface ExcelColor {
    void applyBackground(CellStyle cellStyle);
}
