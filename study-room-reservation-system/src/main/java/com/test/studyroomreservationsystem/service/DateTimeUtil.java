package com.test.studyroomreservationsystem.service;

import java.time.*;

public class DateTimeUtil {

    public static ZonedDateTime getZonedStartOfToday() {
        LocalDate localDate = LocalDate.now();
        return localDate.atStartOfDay(ZoneId.of("Asia/Seoul"));
    }
    public static Instant getInstantStartOfToday() {
        return getZonedStartOfToday().toInstant();
    }

    public static ZonedDateTime getZonedEndOfToday() {
        LocalDate localDate = LocalDate.now();
        return localDate.plusDays(1).atStartOfDay(ZoneId.of("Asia/Seoul")).minusNanos(1);
    }
    public static Instant getInstantEndOfToday() {
        return getZonedEndOfToday().toInstant();
    }
    public static Instant getInstantCurrent() {
        return Instant.now();
    }
    public static Instant getInstantStartOfDate(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
    public static Instant getInstantEndOfDate(LocalDate date) {
        return date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1).toInstant();
    }

    public static ZonedDateTime getOneDayBefore(ZonedDateTime zonedDateTime) {
        return zonedDateTime.minusDays(1);
    }
    public static ZonedDateTime getMonthBefore(ZonedDateTime zonedDateTime, Long month) {
        return zonedDateTime.minusMonths(month);
    }

    public static Instant getInstantMonthBefore(Instant instant, Long months) {
        // Instant를 LocalDateTime으로 변환 (UTC 기준)
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        // 주어진 개월 수를 뺌
        LocalDateTime updatedDateTime = localDateTime.minusMonths(months);
        // 다시 LocalDateTime을 Instant로 변환 (UTC 기준)
        return updatedDateTime.toInstant(ZoneOffset.UTC);
    }

    public static Instant getInstantMonthAfter(Instant instant, Long months) {
        // Instant를 LocalDateTime으로 변환 (UTC 기준)
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        // 주어진 개월 수를 뺌
        LocalDateTime updatedDateTime = localDateTime.plusMonths(months);
        // 다시 LocalDateTime을 Instant로 변환 (UTC 기준)
        return updatedDateTime.toInstant(ZoneOffset.UTC);
    }

    public static LocalDate getDateOfInstant(Instant instant) {
        return LocalDate.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }

    // Convert UTC to KST
    public static LocalDateTime convertUtcToKst(Instant utcDateTime) {
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

    public static LocalDateTime convertKstToUtc(ZonedDateTime kstDateTime) {
        ZonedDateTime utcZonedDateTime = kstDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZonedDateTime.toLocalDateTime();
    }
}

