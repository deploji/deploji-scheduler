package com.deploji.scheduler.models;

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
    private Integer dayOfMonth;
    @NotNull
    @Size(min = 1)
    private List<Month> months;
}
