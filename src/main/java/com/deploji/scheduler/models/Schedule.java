package com.deploji.scheduler.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;

@Data
@Document
public class Schedule {
    @Id
    private String id;
    @Valid
    private Job job;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime endOn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime startFrom;
    @Valid
    private Daily daily;
    @Valid
    private Weekly weekly;
    @Valid
    private Monthly monthly;
    private String cronExpression;
}
