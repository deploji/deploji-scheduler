package com.deploji.scheduler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Daily {
    @NotNull
    @Min(1)
    @JsonProperty("Every")
    private Integer every;
}
