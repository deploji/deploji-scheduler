package com.deploji.scheduler.tasks;

import com.deploji.scheduler.models.Job;
import com.deploji.scheduler.models.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;

public class CreateJobTask implements Runnable {
    private Logger logger = LoggerFactory.getLogger(CreateJobTask.class);
    private Job job;
    private Schedule schedule;
    private String apiUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public CreateJobTask(Job job, Schedule schedule, String apiUrl) {
        this.job = job;
        this.schedule = schedule;
        this.apiUrl = apiUrl;
    }

    @Override
    public void run() {
        if (schedule.getStartFrom() != null && schedule.getStartFrom().isAfter(ZonedDateTime.now())) {
            return;
        }
        if (schedule.getEndOn() != null && schedule.getEndOn().isBefore(ZonedDateTime.now())) {
            return;
        }
        logger.info("Calling Deploji API {} to create job {}", apiUrl, job);
        try {
            ResponseEntity<Job> response = restTemplate.exchange(apiUrl + "/jobs", HttpMethod.POST, new HttpEntity<>(job), Job.class);
            logger.info("Response code {}", response.getStatusCodeValue());
        } catch (Exception e) {
            logger.error("Error calling deploji API: {}", e.getMessage());
        }
    }
}
