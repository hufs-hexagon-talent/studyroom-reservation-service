package hufs.computer.studyroom.common.util;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiResponseListDto<T> {
        private List<T> items;
        public ApiResponseListDto() {}
        public ApiResponseListDto(List<T> items) {
            this.items = items;
        }

    }


