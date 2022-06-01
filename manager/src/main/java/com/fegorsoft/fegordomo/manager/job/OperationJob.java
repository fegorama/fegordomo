package com.fegorsoft.fegordomo.manager.job;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fegorsoft.fegordomo.manager.dto.OperationMessageDTO;
import com.fegorsoft.fegordomo.manager.messages.MQTTService;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class OperationJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(OperationJob.class);

    @Autowired
    private MQTTService mqttService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String deviceGroupName = (String) jobDataMap.get("deviceGroupName");
        long deviceId = (long) jobDataMap.get("deviceId");
        String deviceName = (String) jobDataMap.get("deviceName");
        long operationId = (long) jobDataMap.get("operationId");
        String data = (String) jobDataMap.get("data");

        Trigger trigger = jobExecutionContext.getTrigger();

        log.info("Trigger: {}", trigger.getDescription());

        // 1 = ON, 0 = OFF
       //********  int mode = trigger.getKey().getName().indexOf("-on") != -1 ? 1 : 0;

        try {
            sendMessageToMqtt(deviceId, deviceName, operationId, data, deviceGroupName);

        } catch (IOException ioe) {
            log.error("Error sending message to device!");
        }
    }

    private void sendMessageToMqtt(long deviceId, String deviceName, long operationId, String data, String deviceGroupName) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        OperationMessageDTO message = new OperationMessageDTO(deviceId, deviceName, operationId, data, System.currentTimeMillis());

        String jsonInputString = objectMapper.writeValueAsString(message);
        mqttService.pub(jsonInputString, deviceGroupName);

    }
}
