package com.fegorsoft.fegordomo.manager.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class GPIOJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(GPIOJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        System.out.println("Executing Job");
        /*
         * JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap(); String
         * device = jobDataMap.getString("device"); String gpio =
         * jobDataMap.getString("gpio"); String mode = jobDataMap.getString("mode");
         * String action = jobDataMap.getString("action");
         */

        // TODO Según acción (on/off): pinWrite...
    }
}
