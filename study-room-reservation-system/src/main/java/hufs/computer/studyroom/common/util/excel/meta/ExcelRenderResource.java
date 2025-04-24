package hufs.computer.studyroom.common.util.excel.meta;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ExcelRenderResource {

//    역할: SXSSFExcelFile 이 DTO->셀 변환 시 단순 조회 전용 객체로만 사용하도록 분리 (메타데이터 수정 불가)
    private final ExcelMetaData meta;

    public List<String> getDataFieldNames() { return meta.getDataFieldNames(); }
    public String getExcelHeaderName(String field) { return meta.getExcelHeaderName(field); }

    public enum ExcelRenderLocation {
        HEADER, BODY
    }
}

