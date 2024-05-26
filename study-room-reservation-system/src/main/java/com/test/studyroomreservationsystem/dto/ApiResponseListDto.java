package com.test.studyroomreservationsystem.dto;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiResponseListDto<T> {
        private List<T> items;

        public ApiResponseListDto(List<T> items) {
            this.items = items;
        }

    }


