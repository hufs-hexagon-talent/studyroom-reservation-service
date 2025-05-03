package hufs.computer.studyroom.common.response;

public record PageMeta(
        long totalElements,
        int totalPages,
        int page,
        int size
) { }
