package com.fegorsoft.fegordomo.manager.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fegorsoft.fegordomo.manager.dto.GPIOMessageDTO;
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
public class GPIOJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(GPIOJob.class);

    @Autowired
    private MQTTService mqttService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        int gpio = (int) jobDataMap.get("gpio");
        String ip = (String) jobDataMap.get("ip");

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
            sendMessageToMqtt(ip, gpio, mode);

        } catch (IOException ioe) {
            log.error("Error sending message to device!");
        }
    }

    private void sendMessageToMqtt(String deviceName, int gpio, int mode) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        GPIOMessageDTO message = new GPIOMessageDTO(String.valueOf(gpio), "2", String.valueOf(mode));

        String jsonInputString = objectMapper.writeValueAsString(message);
        mqttService.pub(jsonInputString, deviceName);

    }
}
