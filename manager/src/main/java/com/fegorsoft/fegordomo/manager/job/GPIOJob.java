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

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
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

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        //long gpioId = (long) jobDataMap.get("getId");
        int gpio = (int) jobDataMap.get("gpio");
        String ip = (String) jobDataMap.get("ip");
        String cronTriggerOn = (String) jobDataMap.get("cronTriggerOn");
        String crontriggerOff = (String) jobDataMap.get("crontriggerOff");

        Trigger trigger = jobExecutionContext.getTrigger();

        log.info("Trigger: {}", trigger);

        // 1 = ON, 0 = OFF
        int mode = trigger.getKey().getName().indexOf("-on") != -1 ? 1 : 0;

        try {
            sendMessageToDevice(ip, gpio, mode);

        } catch (IOException ioe) {
            log.error("Error sending message to device!");
        }
    }

    private void sendMessageToDevice(String ip, int gpio, int mode) throws IOException {
        URL url = new URL("http://" + ip + ":8080/gpios/simulation");
log.info("URL: {}", url.toString());
        // Request
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        log.info("Connection: {}", con);

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        GPIOMessageDTO message = new GPIOMessageDTO("15", "2", String.valueOf(mode));

        String jsonInputString = objectMapper.writeValueAsString(message);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Response
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            log.info(response.toString());
        }
    }
}
