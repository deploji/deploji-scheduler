package com.deploji.scheduler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document
public class Schedule {
    @Id
    @JsonProperty("ID")
    private String id;

    @Valid
    @JsonProperty("Job")
    private Job job;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("EndOn")
    private ZonedDateTime endOn;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("StartFrom")
    private ZonedDateTime startFrom;

    @Valid
    @JsonProperty("Daily")
    private Daily daily;

    @Valid
    @JsonProperty("Weekly")
    private Weekly weekly;

    @Valid
    @JsonProperty("Monthly")
    private Monthly monthly;

    @JsonProperty("CronExpression")
    private String cronExpression;

    @JsonProperty("NextExecutionTimes")
    private List<Date> nextExecutionTimes;
}
