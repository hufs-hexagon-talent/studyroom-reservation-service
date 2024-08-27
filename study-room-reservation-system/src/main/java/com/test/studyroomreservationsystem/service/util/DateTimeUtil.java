package com.test.studyroomreservationsystem.service.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static ZonedDateTime getZonedStartOfToday() {
        LocalDate localDate = LocalDate.now();
        return localDate.atStartOfDay(KST_ZONE_ID);
    }
    public static Instant getInstantStartOfToday() {
        return getZonedStartOfToday().toInstant();
    }

    public static ZonedDateTime getZonedEndOfToday() {
        LocalDate localDate = LocalDate.now();
        return localDate.plusDays(1).atStartOfDay(KST_ZONE_ID).minusNanos(1);
    }
    public static Instant getInstantEndOfToday() {
        return getZonedEndOfToday().toInstant();
    }
    public static Instant getInstantCurrent() {
        return Instant.now();
    }
    public static Instant getInstantStartOfDate(LocalDate date) {
        return date.atStartOfDay(KST_ZONE_ID).toInstant();
    }
    public static Instant getInstantEndOfDate(LocalDate date) {
        return date.plusDays(1).atStartOfDay(KST_ZONE_ID).minusNanos(1).toInstant();
    }

    public static Instant getInstantOneDayAfterStartOfDay(Instant instant) {
        // Instant를 ZonedDateTime으로 변환하여 KST 타임존을 적용
        ZonedDateTime zonedDateTime = instant.atZone(KST_ZONE_ID);
        // 현재 시간에서 하루를 더하고, 그 날의 시작 시간을 설정
        ZonedDateTime nextDayStart = zonedDateTime.plusDays(1).toLocalDate().atStartOfDay(KST_ZONE_ID);
        return nextDayStart.toInstant();
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
    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, KST_ZONE_ID);
    }
    public static LocalDate getDateOfInstant(Instant instant) {
        return LocalDate.ofInstant(instant, KST_ZONE_ID);
    }

    // Convert UTC to KST
    public static LocalDateTime convertUtcToKst(Instant utcDateTime) {
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(UTC_ZONE_ID);
        ZonedDateTime kstZonedDateTime = utcZonedDateTime.withZoneSameInstant(KST_ZONE_ID);
        return kstZonedDateTime.toLocalDateTime();
    }

    // Convert KST to UTC
    public static LocalDateTime convertKstToUtc(LocalDateTime kstDateTime) {
        ZonedDateTime kstZonedDateTime = kstDateTime.atZone(KST_ZONE_ID);
        ZonedDateTime utcZonedDateTime = kstZonedDateTime.withZoneSameInstant(UTC_ZONE_ID);
        return utcZonedDateTime.toLocalDateTime();
    }

    public static LocalDateTime convertKstToUtc(ZonedDateTime kstDateTime) {
        ZonedDateTime utcZonedDateTime = kstDateTime.withZoneSameInstant(UTC_ZONE_ID);
        return utcZonedDateTime.toLocalDateTime();
    }
}

