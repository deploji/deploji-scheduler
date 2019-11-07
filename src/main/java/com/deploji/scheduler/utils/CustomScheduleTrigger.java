package com.deploji.scheduler.utils;

import com.deploji.scheduler.models.*;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CustomScheduleTrigger implements Trigger {
    private Schedule schedule;
    private Clock clock;

    public CustomScheduleTrigger(Schedule schedule, Clock clock) {
        this.schedule = schedule;
        this.clock = clock;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        ZonedDateTime start = schedule.getStartFrom();
        ZonedDateTime end = schedule.getEndOn();
        ZonedDateTime now = ZonedDateTime.now(clock);
        if (end != null && end.isBefore(now)) {
            return null;
        }
        if (schedule.getDaily() != null) {
            return nextExecutionTime(start, now, schedule.getDaily());
        }
        if (schedule.getWeekly() != null) {
            return nextExecutionTime(start, now, schedule.getWeekly());
        }
        if (schedule.getMonthly() != null) {
            return nextExecutionTime(start, now, schedule.getMonthly());
        }
        return null;
    }

    public Date nextExecutionTime(ZonedDateTime start, ZonedDateTime now, Monthly monthly) {
        return null;
    }

    public Date nextExecutionTime(ZonedDateTime start, ZonedDateTime now, Weekly weekly) {
        return null;
    }

    public Date nextExecutionTime(ZonedDateTime start, ZonedDateTime now, Daily daily) {
        if (start.isAfter(now)) {
            return Date.from(start.toInstant());
        }
        long diff = ChronoUnit.DAYS.between(start, now);
        ZonedDateTime next = start.plusDays(diff);
        if (next.isBefore(now)) {
            next = next.plusDays(1L);
        }
        return Date.from(next.toInstant());
    }
}
