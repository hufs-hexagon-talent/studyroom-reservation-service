package hufs.computer.studyroom.common.util.excel.meta;

import hufs.computer.studyroom.common.util.excel.annotation.ExcelColumn;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static hufs.computer.studyroom.common.util.reflection.SuperClassReflectionUtils.getAllFields;

/**
 * @ExcelColumn 어노테이션의 headerStyle/bodyStyle 설정을 스캔하여
 * 각 필드별 CellStyle 인스턴스를 캐싱
 */
@Getter
public class ExcelStyleMetaData {
    private final Map<ExcelCellKey, CellStyle> styleMap = new HashMap<>();

    public ExcelStyleMetaData(Class<?> dtoType, SXSSFWorkbook workbook) {
        for (Field field : getAllFields(dtoType)) {
            if (!field.isAnnotationPresent(ExcelColumn.class)) continue;

            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);

            String fieldName = field.getName();

            // Style cache

            styleMap.put(new ExcelCellKey(fieldName, ExcelRenderLocation.HEADER), ExcelStyleMetaDataFactory.create(workbook, annotation.headerStyle()));
            styleMap.put(new ExcelCellKey(fieldName, ExcelRenderLocation.BODY), ExcelStyleMetaDataFactory.create(workbook, annotation.bodyStyle()));
        }
    }

    public CellStyle getCellStyle(String fieldName, ExcelRenderLocation location) {
        return styleMap.get(new ExcelCellKey(fieldName, location));
    }
}
