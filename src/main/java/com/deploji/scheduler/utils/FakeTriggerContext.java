package com.deploji.scheduler.utils;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FakeTriggerContext implements TriggerContext {
    private Date lastScheduledExecutionTime;

    public FakeTriggerContext(Date lastScheduledExecutionTime) {
        this.lastScheduledExecutionTime = lastScheduledExecutionTime;
    }

    @Override
    public Date lastScheduledExecutionTime() {
        return lastScheduledExecutionTime;
    }

    @Override
    public Date lastActualExecutionTime() {
        return lastScheduledExecutionTime;
    }

    @Override
    public Date lastCompletionTime() {
        return lastScheduledExecutionTime;
    }

    public static List<Date> nextExecutionTimes(Trigger trigger) {
        List<Date> dates = new ArrayList<>();
        Date last = null;
        for (int i = 0; i < 10; i++) {
            last = trigger.nextExecutionTime(new FakeTriggerContext(last));
            if (last == null) {
                return dates;
            }
            dates.add(last);
        }
        return dates;
    }
}
