package com.deploji.scheduler.repositories;

import com.deploji.scheduler.models.Schedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ScheduleRepository extends ReactiveMongoRepository<Schedule, String> {
    Flux<Schedule> findAllByJobApplicationIDInAndJobTemplateIDIn(List<Long> applicationId, List<Long> templateId);

    Flux<Schedule> findAllByJobApplicationIDIn(List<Long> applicationId);

    Flux<Schedule> findAllByJobTemplateIDIn(List<Long> templateId);
}
