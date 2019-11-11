package com.deploji.scheduler.utils;

import com.deploji.scheduler.models.Daily;
import com.deploji.scheduler.models.Monthly;
import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.models.Weekly;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CustomScheduleTrigger implements Trigger {
    private Schedule schedule;

    public CustomScheduleTrigger(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        ZonedDateTime start = schedule.getStartFrom();
        ZonedDateTime next = start;
        ZonedDateTime end = schedule.getEndOn();
        ZonedDateTime last = triggerContext.lastScheduledExecutionTime() == null ?
            null : ZonedDateTime.from(triggerContext.lastScheduledExecutionTime().toInstant().atZone(ZoneId.systemDefault()));
        if (schedule.getDaily() != null) {
            next = nextExecutionTime(start, last, schedule.getDaily());
        }
        if (schedule.getWeekly() != null) {
            next = nextExecutionTime(start, last, schedule.getWeekly());
        }
        if (schedule.getMonthly() != null) {
            next = nextExecutionTime(start, last, schedule.getMonthly());
        }
        if (next == null || next.isAfter(end)) {
            return null;
        }
        if (next.isBefore(start)) {
            return Date.from(start.toInstant());
        }
        return Date.from(next.toInstant());
    }

    public ZonedDateTime nextExecutionTime(ZonedDateTime start, ZonedDateTime last, Monthly monthly) {
        ZonedDateTime next;
        if (last == null) {
            next = start.withDayOfMonth(monthly.getDayOfMonth());
            if (next.isBefore(start)) {
                next = next.plusMonths(1);
            }
        } else {
            next = last.plusMonths(1);
        }
        while (!monthly.getMonths().contains(next.getMonth())) {
            next = next.plusMonths(1);
        }
        return next;
    }

    public ZonedDateTime nextExecutionTime(ZonedDateTime start, ZonedDateTime last, Weekly weekly) {
        DayOfWeek lastDayOfWeek = DayOfWeek.SUNDAY;
        ZonedDateTime next = last == null ? start : last.plusDays(1);
        if (weekly.getWeekdays().isEmpty()) {
            throw new IllegalStateException("weekdays must contain at least one day");
        }
        while (!weekly.getWeekdays().contains(next.getDayOfWeek())) {
            next = next.plusDays(1);
            if (next.getDayOfWeek() == lastDayOfWeek) {
                next = next.plusWeeks(weekly.getEvery() - 1L).plusDays(1);
            }
        }
        return next;
    }

    public ZonedDateTime nextExecutionTime(ZonedDateTime start, ZonedDateTime last,  Daily daily) {
        if (last == null) {
            return start;
        }
        return last.plusDays(daily.getEvery());
    }
}
