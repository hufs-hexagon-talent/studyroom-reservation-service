package com.test.studyroomreservationsystem.service;

import java.time.*;

public class DateTimeUtil {

    public static ZonedDateTime getStartOfTodayZoned() {
        LocalDate localDate = LocalDate.now();
        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
    public static Instant getStartOfTodayInstant() {
        return getStartOfTodayZoned().toInstant();
    }
    public static Instant getStartOfDateInstant(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }



    public static ZonedDateTime getEndOfTodayZoned() {
        LocalDate localDate = LocalDate.now();
        return localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);
    }
    public static Instant getEndOfTodayInstant() {
        return getEndOfTodayZoned().toInstant();
    }
    public static Instant getEndOfDateInstant(LocalDate date) {
        return date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1).toInstant();
    }

    public static ZonedDateTime getOneDayBefore(ZonedDateTime zonedDateTime) {
        return zonedDateTime.minusDays(1);
    }

    public static ZonedDateTime getMonthBefore(ZonedDateTime zonedDateTime, Long month) {
        return zonedDateTime.minusMonths(month);
    }

    public static LocalDate getDateOfInstant(Instant instant) {
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }

    // Convert UTC to KST
    public static LocalDateTime convertUtcToKst(LocalDateTime utcDateTime) {
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime kstZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return kstZonedDateTime.toLocalDateTime();
    }

    // Convert KST to UTC
    public static LocalDateTime convertKstToUtc(LocalDateTime kstDateTime) {
        ZonedDateTime kstZonedDateTime = kstDateTime.atZone(ZoneId.of("Asia/Seoul"));
        ZonedDateTime utcZonedDateTime = kstZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZonedDateTime.toLocalDateTime();
    }
}

