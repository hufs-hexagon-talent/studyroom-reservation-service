package com.test.studyroomreservationsystem.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {

        public static Instant getStartOfToday() {
            LocalDate localDate = LocalDate.now();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            return zonedDateTime.toInstant();
        }

        public static Instant getEndOfToday() {
            LocalDate localDate = LocalDate.now();
            ZonedDateTime zonedDateTime = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);
            return zonedDateTime.toInstant();
        }
    }

