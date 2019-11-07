package com.deploji.scheduler.validators;

import com.deploji.scheduler.models.Schedule;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.Objects;

public class ScheduleValidator implements Validator {
    public boolean supports(Class clazz) {
        return Schedule.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "startFrom", "startFrom.empty");
        ValidationUtils.rejectIfEmpty(e, "job", "job.empty");
        Schedule s = (Schedule) obj;
        if (s.getCronExpression() == null &&
            s.getMonthly() == null &&
            s.getWeekly() == null &&
            s.getDaily() == null) {
            e.reject("one-required", "one of the fields is required: daily, weekly, monthly, cronExpression");
        }
        if (checkExclusive(s.getCronExpression(), s.getMonthly(), s.getWeekly(), s.getDaily())) {
            e.reject("exclusive", "the fields: daily, weekly, monthly, cronExpression are exclusive");
        }
        if (s.getCronExpression() != null && !CronSequenceGenerator.isValidExpression(s.getCronExpression())) {
            e.reject("cronExpression", "Cron expression must consist of six fields <second> <minute> <hour> <day-of-month> <month> <day-of-week>");
        }
    }

    private boolean checkExclusive(Object ...fields) {
        return Arrays.stream(fields).filter(Objects::nonNull).count() > 1;
    }
}
