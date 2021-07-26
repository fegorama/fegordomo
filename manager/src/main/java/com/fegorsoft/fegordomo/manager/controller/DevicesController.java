package com.fegorsoft.fegordomo.manager.controller;

import com.fegorsoft.fegordomo.manager.model.Device;
import com.fegorsoft.fegordomo.manager.repository.DeviceRepository;
import com.fegorsoft.fegordomo.manager.exception.DeviceNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/devices")
@Tag(name = "Devices", description = "API for devices")
public class DevicesController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Operation(summary = "Create a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "device created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/add")
    public ResponseEntity<Device> addNewDevice(
            @Parameter(description = "device object to be created") @RequestBody @Valid Device device) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(linkTo(DevicesController.class).slash(device.getId()).toUri());
        Device savedDevice;

        try {
            savedDevice = deviceRepository.save(device);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(savedDevice, httpHeaders, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a device by its id")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Found the device", 
          content = { @Content(mediaType = "application/json", 
            schema = @Schema(implementation = Device.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", 
          content = @Content), 
        @ApiResponse(responseCode = "404", description = "Device not found", 
          content = @Content) })
    @GetMapping("/{id}")
    public Device findById(@PathVariable Integer id) throws DeviceNotFoundException{
        return deviceRepository.findById(id)
            .orElseThrow(() -> DeviceNotFoundException()));
    }

    @Operation(summary = "Get all devices")
    @GetMapping("/all")
    public @ResponseBody Iterable<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
