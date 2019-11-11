package com.deploji.scheduler.controllers;

import com.deploji.scheduler.models.Schedule;
import com.deploji.scheduler.services.ScheduleService;
import com.deploji.scheduler.validators.ScheduleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/schedules")
public class SchedulesController {
    private Logger logger = LoggerFactory.getLogger(SchedulesController.class);

    private ScheduleService service;

    SchedulesController(ScheduleService service) {
        this.service = service;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ScheduleValidator());
    }

    @GetMapping
    private Flux<Schedule> getAll() {
        return service.findAll();
    }

    @PostMapping
    private Mono<Schedule> save(@RequestBody @Valid Schedule schedule) {
        return service.save(schedule);
    }

    @DeleteMapping("/{id}")
    private Mono<Void> delete(@PathVariable("id") String id) {
        return service.delete(id);
    }
}
