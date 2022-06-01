package com.fegorsoft.fegordomo.manager.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.JobDTO;
import com.fegorsoft.fegordomo.manager.dto.ScheduleOperationDTO;
import com.fegorsoft.fegordomo.manager.dto.TriggerDTO;
import com.fegorsoft.fegordomo.manager.job.OperationJob;
import com.fegorsoft.fegordomo.manager.job.OperationScheduleService;
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

@EnableScheduling
@RestController
@RequestMapping(path = "/scheduler")
@Tag(name = "Scheduler", description = "API for Operation's scheduler")
public class ScheduleOperationController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleOperationController.class);

    @Autowired
    private OperationScheduleService operationScheduleService;

    @Autowired
    private Scheduler scheduler;

    @Operation(summary = "Build job")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "job created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleOperationResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/build")
    public ScheduleOperationResponse build(
            @Parameter(description = "Schedule Operation") @RequestBody @Valid ScheduleOperationDTO scheduleOperation) {

        return buildJob(scheduleOperation);
    }

    @Operation(summary = "Delete job")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "job deleted", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ScheduleOperationResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping(path = "/{operationId}")
    public ResponseEntity<String> delete(@Parameter(description = "Operation ID") long operationId) {

        try {
            if (scheduler.deleteJob(new JobKey("operationId-" + operationId, operationScheduleService.OPERATION_GROUP_JOB))) {
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

    private ScheduleOperationResponse buildJob(ScheduleOperationDTO scheduleOperation) {
        try {
            JobDetail jobDetail = operationScheduleService.buildJobDetail(scheduleOperation);
            logger.info("Job detail description: {}", jobDetail.getDescription());

            Set<CronTrigger> cronTriggers = new HashSet<>();
            CronTrigger cronTrigger = operationScheduleService.buildJobTrigger(jobDetail);
            cronTriggers.add(cronTrigger);
            logger.info("Trigger Description: {}", cronTrigger.getDescription());

//            CronTrigger cronTrigger0 = buildJobTrigger(jobDetail, (byte) 0);
//            cronTriggers.add(cronTrigger0);
//            logger.info("Trigger Next Fire Time: {}", cronTrigger0.getNextFireTime());


            if (scheduleOperation.isActive()) {
                logger.info("Scheduled is active");
                scheduler.scheduleJob(jobDetail, cronTriggers, true);

            } else {
                logger.info("Scheduled not is active");
                List<Trigger> triggers = new ArrayList<>(cronTriggers);
                scheduler.unscheduleJob(triggers.get(0).getKey());
//                scheduler.unscheduleJob(triggers.get(1).getKey());
            }

            return new ScheduleOperationResponse(true, jobDetail.getKey().getName(), jobDetail.getKey().getGroup(),
                    "Operation Scheduled Successfully!");

        } catch (SchedulerException ex) {
            logger.error("Error scheduling Operation", ex);

            return new ScheduleOperationResponse(false, "Error scheduling Operation. Please try later!");
        }
    }

}
