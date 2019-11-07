package com.deploji.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableReactiveMongoRepositories
public class DeplojiSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeplojiSchedulerApplication.class, args);
	}

}
