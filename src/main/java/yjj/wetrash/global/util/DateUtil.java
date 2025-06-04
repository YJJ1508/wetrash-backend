package yjj.wetrash.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
@Component
@Slf4j
public class DateUtil {
    public LocalDateTime[] getLastMonthRange(){
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusMonths(1).withDayOfMonth(1); //지난달 1일
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth()); //지난달 말일
        log.info("start: {}", start.atStartOfDay());
        log.info("end: {}", end.atTime(LocalTime.MAX));
        return new LocalDateTime[]{
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        };
    }
}
