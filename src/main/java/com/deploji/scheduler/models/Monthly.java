package com.deploji.scheduler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Month;
import java.util.List;

@Data
public class Monthly {
    @NotNull
    @Min(1)
    @Max(31)
    @JsonProperty("DayOfMonth")
    private Integer dayOfMonth;

    @NotNull
    @Size(min = 1)
    @JsonProperty("Months")
    private List<Month> months;
}
