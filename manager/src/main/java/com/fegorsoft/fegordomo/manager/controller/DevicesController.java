package com.fegorsoft.fegordomo.manager.controller;

import java.util.List;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.DeviceDTO;
import com.fegorsoft.fegordomo.manager.exception.DeviceGroupNotFoundException;
import com.fegorsoft.fegordomo.manager.exception.DeviceNotFoundException;
import com.fegorsoft.fegordomo.manager.model.Device;
import com.fegorsoft.fegordomo.manager.model.DeviceGroup;
import com.fegorsoft.fegordomo.manager.repository.DeviceGroupRepository;
import com.fegorsoft.fegordomo.manager.repository.DeviceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

@RestController
@RequestMapping(path = "/devices")
@Tag(name = "Devices", description = "API for devices")
public class DevicesController {
    private static final Logger log = LoggerFactory.getLogger(DevicesController.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    
    @Operation(summary = "Create a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "device created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/add")
    public ResponseEntity<Device> add(
            @Parameter(description = "Name of device") @RequestBody @Valid DeviceDTO deviceDTO) throws DeviceGroupNotFoundException {

        Device device = new Device();
        DeviceGroup deviceGroup = deviceGroupRepository.findById(deviceDTO.getDeviceGroupId())
        .orElseThrow(DeviceGroupNotFoundException::new);

        if (log.isDebugEnabled()) {
            log.debug("Device group: {}", deviceGroup.toString());
        }

        try {
            device.setName(deviceDTO.getName());
            device.setType(deviceDTO.getType());
            device.setDescription(deviceDTO.getDescription());
            device.setEnable(deviceDTO.isEnable());
            device.setDeviceGroup(deviceGroup);
            deviceRepository.save(device);

            if (log.isDebugEnabled()) {
                log.debug("New Device: {}", device.toString());
            }

        } catch (Exception e) {
            log.error("Error add device: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(device, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "device updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Device> update(
            @Parameter(description = "Name of device") @RequestBody @Valid DeviceDTO deviceDTO)
            throws DeviceNotFoundException, DeviceGroupNotFoundException {

        Device device = deviceRepository.findById(deviceDTO.getId()).orElseThrow(DeviceNotFoundException::new);

        if (log.isDebugEnabled()) {
            log.debug("Device: {}", device.toString());
        }

        DeviceGroup deviceGroup = deviceGroupRepository.findById(deviceDTO.getDeviceGroupId())
        .orElseThrow(DeviceGroupNotFoundException::new);

        if (log.isDebugEnabled()) {
            log.debug("Device group: {}", deviceGroup.toString());
        }

        try {
            device.setName(deviceDTO.getName());
            device.setType(deviceDTO.getType());
            device.setDescription(deviceDTO.getDescription());
            device.setEnable(deviceDTO.isEnable());
            device.setDeviceGroup(deviceGroup);
            deviceRepository.save(device);

        } catch (Exception e) {
            log.error("Error update device: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(device, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the device", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found", content = @Content) })
    @GetMapping("/{id}")
    public Device findById(@PathVariable Long id) throws DeviceNotFoundException {
        return deviceRepository.findById(id).orElseThrow(DeviceNotFoundException::new);
    }

    @Operation(summary = "Get all devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found devices", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Devices not found", content = @Content) })
    @GetMapping("/all")
    // TODO quitar
    //public @ResponseBody Iterable<DeviceDTO> getAll() {
    //    return deviceRepository.findAlltoDTO();
    public @ResponseBody List<Device> getAll() {
        return deviceRepository.findAll();
    }
}
