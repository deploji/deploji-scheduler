package com.deploji.scheduler.models;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Daily {
    @NotNull
    @Min(1)
    private Integer every;
}
