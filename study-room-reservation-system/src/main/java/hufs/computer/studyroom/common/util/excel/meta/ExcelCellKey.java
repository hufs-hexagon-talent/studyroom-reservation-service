package hufs.computer.studyroom.common.util.excel.meta;

/**
 * 스타일 맵의 키로 사용
 *
 * - fieldName: DTO 필드명
 * - location: HEADER or BODY
 */
public record ExcelCellKey(
        String fieldName,
        ExcelRenderLocation location
) {
}
