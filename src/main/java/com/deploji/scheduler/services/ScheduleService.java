package com.deploji.scheduler.services;

import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ScheduleService {
    private ScheduleRepository repository;
    private JobScheduler scheduler;

    public ScheduleService(ScheduleRepository repository, JobScheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }

    public Flux<Schedule> findAll() {
        return this.repository.findAll();
    }

    public Mono<Schedule> save(Schedule schedule) {
        return this.repository.save(schedule).doOnNext(s -> scheduler.schedule(s));
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id).doOnSuccess(aVoid -> scheduler.cancel(id));
    }
}
