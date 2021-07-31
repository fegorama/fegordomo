package com.fegorsoft.fegordomo.manager.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.JobDTO;
import com.fegorsoft.fegordomo.manager.dto.ScheduleGPIO;
import com.fegorsoft.fegordomo.manager.dto.TriggerDTO;
import com.fegorsoft.fegordomo.manager.job.GPIOJob;
import com.fegorsoft.fegordomo.manager.payload.ScheduleGPIOResponse;

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

@EnableScheduling
@RestController
@RequestMapping(path = "/scheduler")
@Tag(name = "Scheduler", description = "API for GPIO's scheduler")
public class ScheduleGPIOController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleGPIOController.class);
    private static final String GPIO_GROUP_JOB = "gpio-jobs";
    private static final String GPIO_GROUP_TRIGGER = "gpio-triggers";

    @Autowired
    private Scheduler scheduler;

    @Operation(summary = "Build job")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "job created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleGPIOResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/build")
    public ScheduleGPIOResponse build(
            @Parameter(description = "Schedule GPIO") @RequestBody @Valid ScheduleGPIO scheduleGPIO) {

        return buildJob(scheduleGPIO);
    }

    @Operation(summary = "Delete job")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "job deleted", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleGPIOResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping(path = "/{gpioId}")
    public ResponseEntity<String> delete(@Parameter(description = "GPIO ID") long gpioId) {

        try {
            if (scheduler.deleteJob(new JobKey("gpioId-" + gpioId, GPIO_GROUP_JOB))) {
                return new ResponseEntity<>(HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (SchedulerException se) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Get all jobs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found jobs", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JobDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Jobs not found", content = @Content) })
    @GetMapping("/all")
    public @ResponseBody Iterable<JobDTO> getAll() throws SchedulerException {
        List<JobDTO> jobsDTO = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                List<TriggerDTO> triggersDTO = new ArrayList<>();

                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

                for (Trigger trigger : triggers) {
                    TriggerDTO triggerDTO = new TriggerDTO();
                    triggerDTO.setName(trigger.getKey().getName());
                    triggerDTO.setDescription(trigger.getDescription());
                    triggerDTO.setNextFireTime(trigger.getNextFireTime());
                    triggersDTO.add(triggerDTO);
                }

                JobDTO jobDTO = new JobDTO();
                jobDTO.setName(jobKey.getName());
                jobDTO.setGroup(jobKey.getGroup());
                jobDTO.setTriggerDTO(triggersDTO);
                jobsDTO.add(jobDTO);
            }

        }

        return jobsDTO;
    }

    private ScheduleGPIOResponse buildJob(ScheduleGPIO scheduleGPIO) {
        try {
            JobDetail jobDetail = buildJobDetail(scheduleGPIO);

            Set<CronTrigger> cronTriggers = new HashSet<>();
            cronTriggers.add(buildJobTrigger(jobDetail, (byte) 1));
            cronTriggers.add(buildJobTrigger(jobDetail, (byte) 0));

            if (scheduleGPIO.isActive()) {
                scheduler.scheduleJob(jobDetail, cronTriggers, true);

            } else {
                List<Trigger> triggers = new ArrayList<>(cronTriggers);
                scheduler.unscheduleJob(triggers.get(0).getKey());
                scheduler.unscheduleJob(triggers.get(1).getKey());
            }

            return new ScheduleGPIOResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(),
                    "GPIO Scheduled Successfully!");

        } catch (SchedulerException ex) {
            logger.error("Error scheduling GPIO", ex);

            return new ScheduleGPIOResponse(false, "Error scheduling GPIO. Please try later!");
        }
    }

    private JobDetail buildJobDetail(ScheduleGPIO scheduleGPIO) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("gpioId", scheduleGPIO.getGpioId());
        jobDataMap.put("gpio", scheduleGPIO.getGpio());
        jobDataMap.put("ip", scheduleGPIO.getIp());
        jobDataMap.put("cronTriggerOn", scheduleGPIO.getCronTriggerOn());
        jobDataMap.put("cronTriggerOff", scheduleGPIO.getCronTriggerOff());

        return JobBuilder.newJob(GPIOJob.class).withIdentity("gpioId-" + scheduleGPIO.getGpioId(), GPIO_GROUP_JOB)
                .withDescription("GPIO Job").usingJobData(jobDataMap).storeDurably().build();
    }

    private CronTrigger buildJobTrigger(JobDetail jobDetail, byte mode) {
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String cronTrigger;

        if (mode != 0) {
            cronTrigger = jobDataMap.getString("cronTriggerOn");
        } else {
            cronTrigger = jobDataMap.getString("cronTriggerOff");
        }

        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName() + "-" + (mode != 0 ? "on" : "off"), GPIO_GROUP_TRIGGER)
                .withDescription("GPIO Trigger for mode " + (mode != 0 ? "on" : "off"))
                .withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger)).build();
    }

}
