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
        int data = (int) jobDataMap.get("data");
     //   String deviceName = (String) jobDataMap.get("deviceName");

        // TODO Eliminar esta información ya que no es necesaria ahora mismo
        String cronTriggerOn = (String) jobDataMap.get("cronTriggerOn");
        String crontriggerOff = (String) jobDataMap.get("crontriggerOff");
        String deviceName = (String) jobDataMap.get("deviceName");

        Trigger trigger = jobExecutionContext.getTrigger();

        log.info("Trigger: {}", trigger.getDescription());

        // 1 = ON, 0 = OFF
        int mode = trigger.getKey().getName().indexOf("-on") != -1 ? 1 : 0;

        try {
            // sendMessageToDevice(ip, gpio, mode);
            sendMessageToMqtt(deviceName, data, mode);

        } catch (IOException ioe) {
            log.error("Error sending message to device!");
        }
    }

    private void sendMessageToMqtt(String deviceName, int gpio, int mode) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        OperationMessageDTO message = new OperationMessageDTO(String.valueOf(gpio), "2", String.valueOf(mode));

        String jsonInputString = objectMapper.writeValueAsString(message);
        mqttService.pub(jsonInputString, deviceName);

    }
}
