package com.deploji.scheduler.tasks;

import com.deploji.scheduler.jwt.JWTUtil;
import com.deploji.scheduler.models.Job;
import com.deploji.scheduler.models.Schedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {
    @Value("${deploji.api.url}")
    private String apiUrl;
    private JWTUtil jwtUtil;

    public TaskFactory(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public CreateJobTask jobTask(Job job, Schedule schedule) {
        return new CreateJobTask(job, schedule, apiUrl, jwtUtil);
    }
}
