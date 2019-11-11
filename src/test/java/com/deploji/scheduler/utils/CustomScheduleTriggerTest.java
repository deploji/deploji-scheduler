package com.deploji.scheduler.utils;

import com.deploji.scheduler.models.Daily;
import com.deploji.scheduler.models.Monthly;
import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.models.Weekly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TriggerContext;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class CustomScheduleTriggerTest {
    private ZoneId zone = ZoneId.of("UTC");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zone);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Clock clock = Clock.fixed(ZonedDateTime.parse("2020-01-01T00:00:00Z", dateFormatter).toInstant(), zone);

    private Schedule dailySchedule;
    private CustomScheduleTrigger dailyTrigger;
    private Schedule weeklySchedule;
    private CustomScheduleTrigger weeklyTrigger;
    private Schedule monthlySchedule;
    private CustomScheduleTrigger monthlyTrigger;

    @MockBean
    TriggerContext triggerContext;

    @BeforeEach
    void beforeEach() {
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        dailySchedule = new Schedule();
        dailySchedule.setStartFrom(ZonedDateTime.now(clock).plusDays(1));
        dailySchedule.setEndOn(ZonedDateTime.now(clock).plusMonths(1));
        Daily daily = new Daily();
        daily.setEvery(3);
        dailySchedule.setDaily(daily);
        dailyTrigger = new CustomScheduleTrigger(dailySchedule);


        weeklySchedule = new Schedule();
        weeklySchedule.setStartFrom(ZonedDateTime.now(clock).plusDays(1));
        weeklySchedule.setEndOn(ZonedDateTime.now(clock).plusMonths(1));
        Weekly weekly = new Weekly();
        weekly.setEvery(3);
        weekly.setWeekdays(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY));
        weeklySchedule.setWeekly(weekly);
        weeklyTrigger = new CustomScheduleTrigger(weeklySchedule);

        monthlySchedule = new Schedule();
        monthlySchedule.setStartFrom(ZonedDateTime.now(clock).plusDays(1));
        monthlySchedule.setEndOn(ZonedDateTime.now(clock).plusYears(1));
        Monthly monthly = new Monthly();
        monthly.setDayOfMonth(3);
        monthly.setMonths(Arrays.asList(Month.JANUARY, Month.MARCH));
        monthlySchedule.setMonthly(monthly);
        monthlyTrigger = new CustomScheduleTrigger(monthlySchedule);
    }

    @Test
    void nextExecutionTime_Daily_startFrom() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = dailyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-01-02 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Daily() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2020-01-01T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = dailyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-01-04 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Daily_endOn() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2020-02-01T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = dailyTrigger.nextExecutionTime(triggerContext);
        assertNull(next);
    }

    @Test
    void nextExecutionTime_Weekly_startFrom() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-01-03 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2020-01-03T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-01-21 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly_endOn() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2020-02-01T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertNull(next);
    }

    @Test
    void nextExecutionTime_Monthly_startFrom() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = monthlyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-01-03 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Monthly() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2020-01-03T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = monthlyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-03-03 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Monthly_endOn() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2021-02-01T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = monthlyTrigger.nextExecutionTime(triggerContext);
        assertNull(next);
    }
}
