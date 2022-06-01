package com.fegorsoft.fegordomo.manager.job;

import com.fegorsoft.fegordomo.manager.dto.ScheduleOperationDTO;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;

public interface OperationJobable {
    public final String OPERATION_GROUP_JOB = "operation-jobs";
    public final String OPERATION_GROUP_TRIGGER = "operation-triggers";

    public JobDetail buildJobDetail(ScheduleOperationDTO scheduleOperation);
    public CronTrigger buildJobTrigger(JobDetail jobDetail);
    public boolean deleteOperationJob(long operationId) throws SchedulerException;
}
