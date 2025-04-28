package hufs.computer.studyroom.common.util.excel.meta;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * 렌더링 전용 읽기 전용 객체
 *
 * - ExcelMetaData 와 ExcelStyleMetaData 를 묶어 제공
 * - SXSSFExcelFile 에서만 사용 되며 수정 불가
 */
@RequiredArgsConstructor
public class ExcelRenderResource {

//    역할: SXSSFExcelFile 이 DTO->셀 변환 시 단순 조회 전용 객체로만 사용하도록 분리 (메타데이터 수정 불가)
    private final ExcelMetaData meta;
    private final ExcelStyleMetaData styleMeta;

    public List<String> getDataFieldNames() {
        return meta.getDataFieldNames();
    }

    public String getExcelHeaderName(String field) {
        return meta.getExcelHeaderName(field);
    }

    public CellStyle getCellStyle(String fieldName, ExcelRenderLocation location) {
        return styleMeta.getCellStyle(fieldName, location);
    }
}

