package com.fegorsoft.fegordomo.manager.controller;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.OperationDTO;
import com.fegorsoft.fegordomo.manager.dto.OperationMessageDTO;
import com.fegorsoft.fegordomo.manager.dto.ScheduleOperationDTO;
import com.fegorsoft.fegordomo.manager.exception.DeviceGroupNotFoundException;
import com.fegorsoft.fegordomo.manager.exception.DeviceNotFoundException;
import com.fegorsoft.fegordomo.manager.exception.OperationNotFoundException;
import com.fegorsoft.fegordomo.manager.job.OperationScheduleService;
import com.fegorsoft.fegordomo.manager.model.Device;
import com.fegorsoft.fegordomo.manager.model.DeviceGroup;
import com.fegorsoft.fegordomo.manager.model.Operation;
import com.fegorsoft.fegordomo.manager.repository.DeviceGroupRepository;
import com.fegorsoft.fegordomo.manager.repository.DeviceRepository;
import com.fegorsoft.fegordomo.manager.repository.OperationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/operations")
@Tag(name = "Operations", description = "API for Operations")
public class OperationsController {
        private static final Logger log = LoggerFactory.getLogger(OperationsController.class);

        @Autowired
        private OperationRepository operationRepository;

        @Autowired
        private DeviceRepository deviceRepository;

        @Autowired
        private DeviceGroupRepository deviceGroupRepository;

        @Autowired
        private OperationScheduleService operationScheduleService;

        @Autowired
        private ScheduleOperationController scheduleOperationControllertionRepository;

        @io.swagger.v3.oas.annotations.Operation(summary = "Simulation of send message to device")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "data created", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = OperationMessageDTO.class)) }),
                        @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
        @PostMapping(path = "/simulation")
        public ResponseEntity<OperationMessageDTO> simulation(
                        @Parameter(description = "Message data") @RequestBody @Valid OperationMessageDTO operationMessageDTO) {

                log.info("Message: {}", operationMessageDTO);

                return new ResponseEntity<>(operationMessageDTO, HttpStatus.OK);
        }

        @io.swagger.v3.oas.annotations.Operation(summary = "Create a operation")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "operation created", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)) }),
                        @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
        @PostMapping(path = "/add")
        public ResponseEntity<Operation> add(
                        @Parameter(description = "Name of Operation") @RequestBody @Valid OperationDTO operationDTO)
                        throws DeviceNotFoundException, DeviceGroupNotFoundException {

                Operation operation = new Operation();
                Device device = deviceRepository.findById(operationDTO.getDeviceId())
                                .orElseThrow(DeviceNotFoundException::new);

                if (log.isDebugEnabled()) {
                        log.debug("Device: {}", device.toString());
                }

                DeviceGroup deviceGroup = deviceGroupRepository.findById(device.getDeviceGroup().getId())
                                .orElseThrow(DeviceGroupNotFoundException::new);

                device.setDeviceGroup(deviceGroup);

                try {
                        operation.setMode(operationDTO.getMode());
                        operation.setData(operationDTO.getData());
                        operation.setCronTriggerOn(operationDTO.getCronTriggerOn());
                        operation.setCronTriggerOff(operationDTO.getCronTriggerOff());
                        operation.setDevice(device);
                        operationRepository.save(operation);

                        if (log.isDebugEnabled()) {
                                log.debug("New Operation: {}", operation.toString());
                        }

                        ScheduleOperationDTO scheduleOperation = new ScheduleOperationDTO(
                                        device.getDeviceGroup().getId(), device.getDeviceGroup().getName(),
                                        operation.getId(),
                                        device.getId(),
                                        device.getName(),
                                        operation.getData(),
                                        operation.getCronTriggerOn(),
                                        operation.getCronTriggerOff(), true);
                        scheduleOperationControllertionRepository.build(scheduleOperation);

                        log.info("Add Schedule Operation: device id = device name = {}",
                                        scheduleOperation.getDeviceId(), scheduleOperation.getDeviceName());

                } catch (Exception e) {
                        log.error("Error add device: {}", e.getMessage());
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>(operation, HttpStatus.CREATED);
        }

        @io.swagger.v3.oas.annotations.Operation(summary = "Update a operation")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "operation updated", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)) }),
                        @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
        @PutMapping(path = "/{id}")
        public ResponseEntity<Operation> update(
                        @Parameter(description = "Name of Operation") @RequestBody @Valid OperationDTO operationDTO)
                        throws OperationNotFoundException, DeviceNotFoundException, DeviceGroupNotFoundException {
log.info("******** operationDTO.getId = {}", operationDTO.getId());
                Operation operation = operationRepository.findById(operationDTO.getId())
                                .orElseThrow(OperationNotFoundException::new);

                if (log.isDebugEnabled()) {
                        log.debug("Operation: {}", operation.toString());
                }
log.info("******** {}", operationDTO.getDeviceId());

                Device device = deviceRepository.findById(operationDTO.getDeviceId())
                                .orElseThrow(DeviceNotFoundException::new);

                if (log.isDebugEnabled()) {
                        log.debug("Device: {}", device.toString());
                }
                
                operation.setDevice(device);

                DeviceGroup deviceGroup = deviceGroupRepository.findById(device.getDeviceGroup().getId())
                                .orElseThrow(DeviceGroupNotFoundException::new);

                device.setDeviceGroup(deviceGroup);

                try {
                        operation.setMode(operationDTO.getMode());
                        operation.setData(operationDTO.getData());
                        operation.setCronTriggerOn(operationDTO.getCronTriggerOn());
                        operation.setCronTriggerOff(operationDTO.getCronTriggerOff());
                        operation.setDevice(device);
                        operationRepository.save(operation);

                        log.info("Update operation: id = {}, data = {}", operation.getId(), operation.getData());

                        ScheduleOperationDTO scheduleOperation = new ScheduleOperationDTO(
                                        device.getDeviceGroup().getId(), device.getDeviceGroup().getName(),
                                        operation.getId(),
                                        device.getId(),
                                        device.getName(),
                                        operation.getData(),
                                        operation.getCronTriggerOn(),
                                        operation.getCronTriggerOff(), true);
                        scheduleOperationControllertionRepository.build(scheduleOperation);

                        log.info("Update Schedule Operation: device id = device name = {}",
                                        scheduleOperation.getDeviceId(), scheduleOperation.getDeviceName());

                } catch (Exception e) {
                        log.error("Error update device: {}", e.getMessage());
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>(operation, HttpStatus.OK);
        }

        @io.swagger.v3.oas.annotations.Operation(summary = "Get a device by its id")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the Operation", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Operation not found", content = @Content) })
        @GetMapping("/{id}")
        public OperationDTO getById(@PathVariable Long id) {

                return operationRepository.findByIdtoDTO(id);
        }

        @io.swagger.v3.oas.annotations.Operation(summary = "Get all Operations")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Operations", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Operations not found", content = @Content) })
        @GetMapping("/all")
        public @ResponseBody Iterable<OperationDTO> getAll() {

                return operationRepository.findAlltoDTO();
        }

        @io.swagger.v3.oas.annotations.Operation(summary = "Get all Operations from device")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found Operations", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)) }),
                        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Operations not found", content = @Content) })
        @GetMapping("/device/{deviceId}")
        public @ResponseBody Iterable<OperationDTO> getByDeviceId(@PathVariable Long deviceId) {

                return operationRepository.findByDeviceIdtoDTO(deviceId);
        }

        @io.swagger.v3.oas.annotations.Operation(summary = "Delete operation")
        @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "operation deleted", content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = Operation.class)) }),
                        @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
        @DeleteMapping(path = "/{operationId}")
        public ResponseEntity<String> delete(@Parameter(description = "Operation ID") long id) {

                try {

                        if (operationScheduleService.deleteOperationJob(id)) {
                                deviceRepository.deleteById(id);
                                return new ResponseEntity<>(HttpStatus.OK);

                        } else {
                                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        }

                } catch (Exception e) {
                        log.error("Error delete device group: {}", e.getMessage());
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

        }
}
