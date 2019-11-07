package com.deploji.scheduler.utils;

import com.deploji.scheduler.models.Daily;
import com.deploji.scheduler.models.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CustomScheduleTriggerTest {
    private Schedule schedule;
    private CustomScheduleTrigger trigger;
    private Clock clock = Clock.fixed(new Date(2000, Calendar.JANUARY, 1, 12, 10, 30).toInstant(), ZoneId.systemDefault());

    @BeforeEach
    void beforeEach() {
    }

    @Test
    void nextExecutionTimeDaily() {
        schedule = new Schedule();
        schedule.setStartFrom(ZonedDateTime.now(clock));
        Daily daily = new Daily();
        daily.setEvery(2);
        schedule.setDaily(daily);
        trigger = new CustomScheduleTrigger(schedule, clock);
        Date next = trigger.nextExecutionTime(null);
        assertEquals(next.getTime(), new Date(2000, Calendar.JANUARY, 1, 12, 10, 30).getTime());
        Date next = trigger.nextExecutionTime(null);
        assertEquals(next.getTime(), new Date(2000, Calendar.JANUARY, 1, 12, 10, 30).getTime());
    }
}
