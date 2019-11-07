package com.deploji.scheduler.models;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;
import java.util.List;

@Data
public class Weekly {
    @NotNull
    @Min(1)
    private Integer every;
    @NotNull
    @Size(min = 1, max = 7)
    private List<DayOfWeek> weekdays;
}
