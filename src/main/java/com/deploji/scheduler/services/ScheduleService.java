package com.deploji.scheduler.services;

import com.deploji.scheduler.jwt.AuthFacade;
import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.repositories.ScheduleRepository;
import com.deploji.scheduler.utils.CustomScheduleTrigger;
import com.deploji.scheduler.utils.FakeTriggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ScheduleService {
    private Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    private ScheduleRepository repository;
    private JobScheduler scheduler;

    public ScheduleService(ScheduleRepository repository, JobScheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }

    public Flux<Schedule> findAll() {
        return this.repository.findAll()
            .map(schedule -> {
                Trigger trigger;
                if (schedule.getCronExpression() != null) {
                    trigger = new CronTrigger(schedule.getCronExpression());
                } else {
                    trigger = new CustomScheduleTrigger(schedule);
                }
                schedule.setNextExecutionTimes(FakeTriggerContext.nextExecutionTimes(trigger));
                return schedule;
            });
    }

    public Mono<Schedule> save(Schedule schedule) {
        return Mono.zip(this.repository.save(schedule), AuthFacade.getUser())
            .map(objects -> {
                logger.info("{}", objects.getT2().getId());
                objects.getT1().getJob().setUserID(objects.getT2().getId());
                return objects.getT1();
            })
            .doOnNext(s -> scheduler.schedule(s));
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id).doOnSuccess(aVoid -> scheduler.cancel(id));
    }
}
