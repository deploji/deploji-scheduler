package com.deploji.scheduler.repositories;

import com.deploji.scheduler.models.Schedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ScheduleRepository extends ReactiveMongoRepository<Schedule, String> {
    Mono<Schedule> findFirstById(Mono<String> id);
}
