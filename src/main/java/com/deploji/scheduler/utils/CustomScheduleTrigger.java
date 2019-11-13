package com.deploji.scheduler.utils;

import com.deploji.scheduler.models.Daily;
import com.deploji.scheduler.models.Monthly;
import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.models.Weekly;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CustomScheduleTrigger implements Trigger {
    private Schedule schedule;
    private Clock clock;

    public CustomScheduleTrigger(Schedule schedule) {
        this.schedule = schedule;
        this.clock = Clock.systemDefaultZone();
    }

    public CustomScheduleTrigger(Schedule schedule, Clock clock) {
        this.schedule = schedule;
        this.clock = clock;
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
        return Date.from(next.toInstant());
    }

    private ZonedDateTime nextExecutionTime(ZonedDateTime start, ZonedDateTime last, Monthly monthly) {
        ZonedDateTime next;
        ZonedDateTime now = ZonedDateTime.now(clock);
        if (last != null) {
            next = last.plusMonths(1);
        } else if (start.isAfter(now)) {
            next = start.withDayOfMonth(monthly.getDayOfMonth());
            if (next.isBefore(start)) {
                next = next.plusMonths(1);
            }
        } else {
            next = now.withDayOfMonth(monthly.getDayOfMonth());
            if (now.getDayOfMonth() > monthly.getDayOfMonth()) {
                next = next.plusMonths(1);
            }
        }
        while (!monthly.getMonths().contains(next.getMonth())) {
            next = next.plusMonths(1);
        }
        return next;
    }

    private ZonedDateTime nextExecutionTime(ZonedDateTime start, ZonedDateTime last, Weekly weekly) {
        if (weekly.getWeekdays().isEmpty()) {
            throw new IllegalStateException("weekdays must contain at least one day");
        }
        DayOfWeek lastDayOfWeek = DayOfWeek.SUNDAY;
        ZonedDateTime next;
        ZonedDateTime now = ZonedDateTime.now(clock);
        if (last != null) {
            next = nextCandidate(weekly, lastDayOfWeek, last);
        } else if (start.isAfter(now)) {
            next = start;
        } else {
            long diff = ChronoUnit.WEEKS.between(start, now);
            long total = (diff / weekly.getEvery()) * weekly.getEvery();
            if (diff % weekly.getEvery() != 0) {
                total += weekly.getEvery();
            }
            next = start.plusWeeks(total);
            next = next.minusDays(next.getDayOfWeek().ordinal());
        }
        while (!weekly.getWeekdays().contains(next.getDayOfWeek())) {
            next = nextCandidate(weekly, lastDayOfWeek, next);
        }
        return next;
    }

    private ZonedDateTime nextCandidate(Weekly weekly, DayOfWeek lastDayOfWeek, ZonedDateTime next) {
        if (next.getDayOfWeek() == lastDayOfWeek) {
            next = next.plusWeeks(weekly.getEvery()).minusDays(6);
        } else {
            next = next.plusDays(1);
        }
        return next;
    }

    private ZonedDateTime nextExecutionTime(ZonedDateTime start, ZonedDateTime last, Daily daily) {
        if (last != null) {
            return last.plusDays(daily.getEvery());
        }
        ZonedDateTime now = ZonedDateTime.now(clock);
        if (start.isAfter(now)) {
            return start;
        }
        long diff = ChronoUnit.DAYS.between(start, now);
        long total = (diff / daily.getEvery()) * daily.getEvery();
        if (diff % daily.getEvery() != 0) {
            total += daily.getEvery();
        }
        return start.plusDays(total);
    }
}
