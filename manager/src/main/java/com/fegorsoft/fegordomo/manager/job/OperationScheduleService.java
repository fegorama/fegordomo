package com.fegorsoft.fegordomo.manager.job;

import com.fegorsoft.fegordomo.manager.dto.ScheduleOperationDTO;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
@EnableScheduling
public class OperationScheduleService implements OperationJobable {
    private static final Logger log = LoggerFactory.getLogger(OperationScheduleService.class);

    @Autowired
    private Scheduler scheduler;

    @Override
    public JobDetail buildJobDetail(ScheduleOperationDTO scheduleOperation) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("deviceGroupId", scheduleOperation.getDeviceGroupId());
        jobDataMap.put("deviceGroupName", scheduleOperation.getDeviceGroupName());
        jobDataMap.put("operationId", scheduleOperation.getOperationId());
        jobDataMap.put("deviceId", scheduleOperation.getDeviceId());
        jobDataMap.put("deviceName", scheduleOperation.getDeviceName());
        jobDataMap.put("data", scheduleOperation.getData());
        jobDataMap.put("cronTriggerOn", scheduleOperation.getCronTriggerOn());
        jobDataMap.put("cronTriggerOff", scheduleOperation.getCronTriggerOff());
        jobDataMap.put("deviceName", scheduleOperation.getDeviceName());

        return JobBuilder.newJob(OperationJob.class).withIdentity("operationId-" + scheduleOperation.getOperationId(), OPERATION_GROUP_JOB)
                .withDescription("Operation Job").usingJobData(jobDataMap).storeDurably().build();
    }

    @Override
    public CronTrigger buildJobTrigger(JobDetail jobDetail) {
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String    cronTrigger = jobDataMap.getString("cronTriggerOn");

        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), OPERATION_GROUP_TRIGGER)
                .withDescription("Operation Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger)).build();
    }

    @Override
    public boolean deleteOperationJob(long operationId) throws SchedulerException {
        
        if (scheduler.deleteJob(new JobKey("operationId-" + operationId, OPERATION_GROUP_JOB))) {
            return true;

        } else {
            return false;
        }        
    }

    
}
