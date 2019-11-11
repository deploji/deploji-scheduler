package com.deploji.scheduler.tasks;

import com.deploji.scheduler.jwt.JWTUtil;
import com.deploji.scheduler.jwt.Role;
import com.deploji.scheduler.jwt.User;
import com.deploji.scheduler.models.Job;
import com.deploji.scheduler.models.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.Collections;

public class CreateJobTask implements Runnable {
    private Logger logger = LoggerFactory.getLogger(CreateJobTask.class);
    private Job job;
    private Schedule schedule;
    private String apiUrl;
    private JWTUtil jwtUtil;
    private RestTemplate restTemplate = new RestTemplate();

    public CreateJobTask(Job job, Schedule schedule, String apiUrl, JWTUtil jwtUtil) {
        this.job = job;
        this.schedule = schedule;
        this.apiUrl = apiUrl;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void run() {
        if (schedule.getStartFrom() != null && schedule.getStartFrom().isAfter(ZonedDateTime.now())) {
            return;
        }
        if (schedule.getEndOn() != null && schedule.getEndOn().isBefore(ZonedDateTime.now())) {
            return;
        }
        User user = new User();
        user.setUsername("scheduler");
        user.setType(Role.ADMIN);
        user.setId(1000L);
        String token = jwtUtil.generateToken(user);
        logger.info(token);
        logger.info("Calling Deploji API {} to create job {}", apiUrl, job);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<Job> httpEntity = new HttpEntity<>(job, headers);
            ResponseEntity<Job> response = restTemplate.exchange(apiUrl + "/jobs", HttpMethod.POST, httpEntity, Job.class);
            logger.info("Response code {}", response.getStatusCodeValue());
        } catch (Exception e) {
            logger.error("Error calling deploji API: {}", e.getMessage());
        }
    }
}
