package com.deploji.scheduler.services;

import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.repositories.ScheduleRepository;
import com.deploji.scheduler.tasks.CreateJobTask;
import com.deploji.scheduler.utils.CustomScheduleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class JobScheduler {
    private Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    private TaskScheduler scheduler;
    @Value("${deploji.api.url}")
    private String apiUrl;
    private Map<String, ScheduledFuture> futures = new HashMap<>();

    public JobScheduler(TaskScheduler scheduler, ScheduleRepository repository) {
        this.scheduler = scheduler;
        logger.info("Scheduling jobs from database");
        repository.findAll().subscribe(this::schedule);
    }

    public void schedule(Schedule schedule) {
        Trigger trigger;
        if (schedule.getCronExpression() != null) {
            logger.info("Scheduling job {} with cron {}", schedule.getJob(), schedule.getCronExpression());
            trigger = new CronTrigger(schedule.getCronExpression());
        } else {
            logger.info("Scheduling job {} with schedule {}", schedule.getJob(), schedule);
            trigger = new CustomScheduleTrigger(schedule, Clock.systemDefaultZone());
        }
        ScheduledFuture future = this.scheduler.schedule(new CreateJobTask(schedule.getJob(), schedule, apiUrl), trigger);
        this.futures.put(schedule.getId(), future);
    }

    public void cancel(String id) {
        logger.info("Canceling scheduled job {}", id);
        ScheduledFuture future = this.futures.get(id);
        if (future == null) {
            logger.error("Scheduled job {} not found", id);
            return;
        }
        future.cancel(true);
    }
}
