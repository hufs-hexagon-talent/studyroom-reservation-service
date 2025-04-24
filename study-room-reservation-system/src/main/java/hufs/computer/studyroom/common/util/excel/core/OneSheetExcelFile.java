package hufs.computer.studyroom.common.util.excel.core;

import java.util.List;

// 구현체 - 단일 시트 전략
public final class OneSheetExcelFile<T> extends SXSSFExcelFile<T> {

    public OneSheetExcelFile(List<T> data, Class<T> dtoType) {
        super(data, dtoType);
    }

    @Override
    public void renderExcel(List<T> data) {
        sheet = workbook.createSheet();
        renderHeaders(sheet, ROW_START_INDEX, COLUMN_START_INDEX);
        if (data.isEmpty()) {
            return;
        }
        int rowIndex = ROW_START_INDEX + 1;
        for (Object renderedData : data) {
            renderBody(renderedData, rowIndex++, COLUMN_START_INDEX);
        }
    }
}
