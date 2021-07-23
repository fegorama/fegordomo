package com.fegorsoft.fegordomo.manager.controller;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.model.GPIO;
import com.fegorsoft.fegordomo.manager.payload.ScheduleGPIOResponse;
import com.fegorsoft.fegordomo.manager.job.GPIOJob;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(path="/gpio")
public class ScheduleGPIO {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleGPIO.class);

    @Autowired
    private Scheduler scheduler;

    @PostMapping("/schedule-gpio")
    public ResponseEntity<ScheduleGPIOResponse> scheduleGPIO(@Valid @RequestBody GPIO gpioRequest) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(gpioRequest.getDateTimeOn(), gpioRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                ScheduleGPIOResponse scheduleGPIOResponse = new ScheduleGPIOResponse(false,
                        "dateTime must be after current time");
                return ResponseEntity.badRequest().body(scheduleGPIOResponse);
            }

            JobDetail jobDetail = buildJobDetail(gpioRequest);
            Trigger trigger = buildJobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            ScheduleGPIOResponse scheduleEmailResponse = new ScheduleGPIOResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
            return ResponseEntity.ok(scheduleEmailResponse);
        } catch (SchedulerException ex) {
            logger.error("Error scheduling email", ex);

            ScheduleGPIOResponse scheduleEmailResponse = new ScheduleGPIOResponse(false,
                    "Error scheduling email. Please try later!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
        }
    }

    private JobDetail buildJobDetail(GPIO scheduleGPIO) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("Device", scheduleGPIO.getDevice());
        jobDataMap.put("GPIO", scheduleGPIO.getGpio());
        jobDataMap.put("Mode", scheduleGPIO.getMode());

        // TODO Cambiar para encender y apagar
        jobDataMap.put("action", "on");

        return JobBuilder.newJob(GPIOJob.class)
                .withIdentity(UUID.randomUUID().toString(), "gpio-jobs")
                .withDescription("GPIO On/Off Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("GPIO On/Off Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

}
