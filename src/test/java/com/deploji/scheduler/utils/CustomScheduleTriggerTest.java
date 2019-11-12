package com.deploji.scheduler.utils;

import com.deploji.scheduler.models.Daily;
import com.deploji.scheduler.models.Monthly;
import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.models.Weekly;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TriggerContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
public class CustomScheduleTriggerTest {
    private ZoneId zone = ZoneId.of("UTC");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zone);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Clock clock = Clock.fixed(ZonedDateTime.parse("2019-11-12T00:00:00Z", dateFormatter).toInstant(), zone);

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
        dailyTrigger = new CustomScheduleTrigger(dailySchedule, clock);

        weeklySchedule = new Schedule();
        weeklySchedule.setStartFrom(ZonedDateTime.now(clock).withDayOfYear(1));
        weeklySchedule.setEndOn(ZonedDateTime.now(clock).plusYears(1));
        Weekly weekly = new Weekly();
        weekly.setEvery(2);
        weekly.setWeekdays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.SUNDAY));
        weeklySchedule.setWeekly(weekly);
        weeklyTrigger = new CustomScheduleTrigger(weeklySchedule, clock);

        monthlySchedule = new Schedule();
        monthlySchedule.setStartFrom(ZonedDateTime.now(clock).plusDays(1));
        monthlySchedule.setEndOn(ZonedDateTime.now(clock).plusYears(1));
        Monthly monthly = new Monthly();
        monthly.setDayOfMonth(3);
        monthly.setMonths(Arrays.asList(Month.JANUARY, Month.MARCH));
        monthlySchedule.setMonthly(monthly);
        monthlyTrigger = new CustomScheduleTrigger(monthlySchedule, clock);
    }

    @Test
    void nextExecutionTime_Daily_startFrom_in_future() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = dailyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-13 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Daily_startFrom_in_past() {
        dailySchedule.setStartFrom(ZonedDateTime.now(clock).minusDays(11));
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = dailyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-13 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Daily() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = dailyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-13 00:00:00", format.format(next));
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
    void nextExecutionTime_Weekly_startFrom_in_future() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-18 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly_startFrom_in_past() {
        weeklySchedule.setStartFrom(ZonedDateTime.now(clock).minusDays(12));
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-11 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-18 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly2() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2019-11-18T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-11-24 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly3() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2019-11-24T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2019-12-02 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly4() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2019-12-30T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-01-05 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Weekly_endOn() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(
            Date.from(ZonedDateTime.parse("2024-02-01T00:00:00Z", dateFormatter).toInstant())
        );
        Date next = weeklyTrigger.nextExecutionTime(triggerContext);
        assertNull(next);
    }

    @Test
    void nextExecutionTime_Weekly_empty_weekdays() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        weeklySchedule.getWeekly().setWeekdays(new ArrayList<>());
        Assertions.assertThatThrownBy(() -> weeklyTrigger.nextExecutionTime(triggerContext)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void nextExecutionTime_Monthly_startFrom_in_future() {
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Clock clock = Clock.fixed(ZonedDateTime.parse("2020-01-10T00:00:00Z", dateFormatter).toInstant(), zone);
        monthlyTrigger = new CustomScheduleTrigger(monthlySchedule, clock);
        monthlySchedule.setStartFrom(ZonedDateTime.now(clock).plusDays(1));
        monthlySchedule.getMonthly().setMonths(Arrays.asList(Month.JANUARY, Month.FEBRUARY, Month.MARCH));
        Date next = monthlyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-02-03 00:00:00", format.format(next));
    }

    @Test
    void nextExecutionTime_Monthly_startFrom_in_past() {
        Clock clock = Clock.fixed(ZonedDateTime.parse("2020-01-10T00:00:00Z", dateFormatter).toInstant(), zone);
        monthlyTrigger = new CustomScheduleTrigger(monthlySchedule, clock);
        monthlySchedule.setStartFrom(ZonedDateTime.now(clock).minusMonths(12));
        Mockito.when(triggerContext.lastScheduledExecutionTime()).thenReturn(null);
        Date next = monthlyTrigger.nextExecutionTime(triggerContext);
        assertEquals("2020-03-03 00:00:00", format.format(next));
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
