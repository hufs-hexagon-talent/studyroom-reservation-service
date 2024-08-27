package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.service.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
public class DataTimeUtilTest {
    @Test
    void check() {
        ZonedDateTime startKST = DateTimeUtil.getZonedStartOfToday();
        ZonedDateTime endKST = DateTimeUtil.getZonedEndOfToday();
        log.info(startKST.toString());
        log.info(endKST.toString());

        Instant startUTC = DateTimeUtil.getInstantStartOfToday();
        Instant endUTC = DateTimeUtil.getInstantEndOfToday();
        log.info(startUTC.toString());
        log.info(endUTC.toString());

        LocalDateTime skst = DateTimeUtil.convertUtcToKst(startUTC);
        LocalDateTime ekst = DateTimeUtil.convertUtcToKst(endUTC);
        log.info(skst.toString());
        log.info(ekst.toString());

        LocalDateTime sutc = DateTimeUtil.convertKstToUtc(startKST);
        LocalDateTime eutc = DateTimeUtil.convertKstToUtc(endKST);
        log.info(sutc.toString());
        log.info(eutc.toString());

    }
}
