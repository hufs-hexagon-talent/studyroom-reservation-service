package com.test.studyroomreservationsystem.dto;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiResponseList<T> {
        private List<T> items;

        public ApiResponseList(List<T> items) {
            this.items = items;
        }

    }


