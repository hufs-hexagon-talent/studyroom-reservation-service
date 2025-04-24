package hufs.computer.studyroom.common.util.excel.meta;

import hufs.computer.studyroom.common.error.code.ExcelErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.util.excel.annotation.ExcelColumn;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static hufs.computer.studyroom.common.util.reflection.SuperClassReflectionUtils.getAllFields;

@Getter
public class ExcelMetaData {
    // 헤더 순서 보존용 LinkedHashMap<필드명, 헤더 텍스트>
    private final Map<String, String> headerNameMap = new LinkedHashMap<>();

    public ExcelMetaData(Class<?> dtoType) {
        List<Field> fields = getAllFields(dtoType);
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                headerNameMap.put(field.getName(), annotation.headerName());
            }
        }

        if (headerNameMap.isEmpty()) {
            throw new CustomException(ExcelErrorCode.INVALID_EXCEL_COLUMN);
        }
    }

    public List<String> getDataFieldNames() {        // 순서 보장
        return List.copyOf(headerNameMap.keySet());
    }

    public String getExcelHeaderName(String field) {
        return headerNameMap.get(field);
    }
}
