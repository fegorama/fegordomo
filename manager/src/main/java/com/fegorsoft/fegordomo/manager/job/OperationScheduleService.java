package com.fegorsoft.fegordomo.manager.job;

import org.springframework.stereotype.Service;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.JobDTO;
import com.fegorsoft.fegordomo.manager.dto.ScheduleOperationDTO;
import com.fegorsoft.fegordomo.manager.dto.TriggerDTO;
import com.fegorsoft.fegordomo.manager.job.OperationJob;
import com.fegorsoft.fegordomo.manager.payload.ScheduleOperationResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
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
