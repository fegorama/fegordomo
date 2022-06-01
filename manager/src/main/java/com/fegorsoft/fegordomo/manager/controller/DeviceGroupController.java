package com.fegorsoft.fegordomo.manager.controller;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.DeviceGroupDTO;
import com.fegorsoft.fegordomo.manager.exception.DeviceGroupNotFoundException;
import com.fegorsoft.fegordomo.manager.model.DeviceGroup;
import com.fegorsoft.fegordomo.manager.repository.DeviceGroupRepository;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/device_groups")
@Tag(name = "DeviceGroup", description = "API for device groups")
public class DeviceGroupController {
    private static final Logger log = LoggerFactory.getLogger(DeviceGroupController.class);

    @Autowired
    private DeviceGroupRepository deviceRepository;

    @Operation(summary = "Create a device group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "device group created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceGroup.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/add")
    public ResponseEntity<DeviceGroup> add(
            @Parameter(description = "Name of device group") @RequestBody @Valid DeviceGroupDTO deviceGroupDTO) {

        DeviceGroup deviceGroup = new DeviceGroup();

        try {
            deviceGroup.setName(deviceGroupDTO.getName());
            deviceRepository.save(deviceGroup);

        } catch (Exception e) {
            log.error("Error add device group: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(deviceGroup, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "device updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceGroup.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PutMapping(path = "/{id}")
    public ResponseEntity<DeviceGroup> update(
            @Parameter(description = "Name of device") @RequestBody @Valid DeviceGroupDTO deviceDTO)
            throws DeviceGroupNotFoundException {

        DeviceGroup deviceGroup = deviceRepository.findById(deviceDTO.getId())
                .orElseThrow(DeviceGroupNotFoundException::new);

        try {
            deviceGroup.setName(deviceDTO.getName());
            deviceRepository.save(deviceGroup);

        } catch (Exception e) {
            log.error("Error update device group: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(deviceGroup, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a device group by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the device group", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceGroup.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Device group not found", content = @Content) })
    @GetMapping("/{id}")
    public DeviceGroup findById(@PathVariable Long id) throws DeviceGroupNotFoundException {
        return deviceRepository.findById(id).orElseThrow(DeviceGroupNotFoundException::new);
    }

    @Operation(summary = "Get all device groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found device groups", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceGroup.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Device groups not found", content = @Content) })
    @GetMapping("/all")
    public @ResponseBody Iterable<DeviceGroupDTO> getAll() {
        return deviceRepository.findAlltoDTO();
    }

    @Operation(summary = "Delete device group")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "device group deleted", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceGroup.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @DeleteMapping(path = "/{deviceGroupId}")
    public ResponseEntity<String> delete(@Parameter(description = "Device group ID") long id) {

        try {
            deviceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error delete device group: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
