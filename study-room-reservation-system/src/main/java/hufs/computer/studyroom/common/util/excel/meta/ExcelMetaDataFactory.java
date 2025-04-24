package hufs.computer.studyroom.common.util.excel.meta;

public final class ExcelMetaDataFactory {
    private ExcelMetaDataFactory() {}

    public static ExcelRenderResource create(Class<?> dtoType) {
        // 애노테이션 스캔
        ExcelMetaData excelMetaData = new ExcelMetaData(dtoType);

        // 읽기 전용 맵핑
        return new ExcelRenderResource(excelMetaData);
    }
}
