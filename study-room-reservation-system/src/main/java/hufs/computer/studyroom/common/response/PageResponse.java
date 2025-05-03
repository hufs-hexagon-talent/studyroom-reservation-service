package hufs.computer.studyroom.common.response;

import java.util.List;

public record PageResponse<T>(PageMeta meta, List<T> items) {
}
