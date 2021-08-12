package com.fegorsoft.fegordomo.manager.controller;

import javax.validation.Valid;

import com.fegorsoft.fegordomo.manager.dto.GPIODTO;
import com.fegorsoft.fegordomo.manager.dto.GPIOMessageDTO;
import com.fegorsoft.fegordomo.manager.dto.ScheduleGPIO;
import com.fegorsoft.fegordomo.manager.exception.DeviceNotFoundException;
import com.fegorsoft.fegordomo.manager.exception.GPIONotFoundException;
import com.fegorsoft.fegordomo.manager.model.Device;
import com.fegorsoft.fegordomo.manager.model.GPIO;
import com.fegorsoft.fegordomo.manager.repository.DeviceRepository;
import com.fegorsoft.fegordomo.manager.repository.GPIORepository;

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
@RequestMapping(path = "/gpios")
@Tag(name = "GPIOS", description = "API for GPIOs")
public class GPIOSController {
    private static final Logger log = LoggerFactory.getLogger(GPIOSController.class);

    @Autowired
    private GPIORepository gpioRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ScheduleGPIOController scheduleGPIOController;

    @Operation(summary = "Simulation of send message to device")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "gpio created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = GPIOMessageDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/simulation")
    public ResponseEntity<GPIOMessageDTO> simulation(
            @Parameter(description = "Message GPIO") @RequestBody @Valid GPIOMessageDTO gpioMessageDTO) {

        log.info("Message: {}", gpioMessageDTO);
        
        return new ResponseEntity<>(gpioMessageDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a gpio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "gpio created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GPIO.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PostMapping(path = "/add")
    public ResponseEntity<GPIO> add(@Parameter(description = "Name of GPIO") @RequestBody @Valid GPIODTO gpioDTO)
            throws DeviceNotFoundException {

        GPIO gpio = new GPIO();
        Device device = deviceRepository.findById(gpioDTO.getDeviceId()).orElseThrow(DeviceNotFoundException::new);

        try {
            gpio.setMode(gpioDTO.getMode());
            gpio.setGpio(gpioDTO.getGpio());
            gpio.setCronTriggerOn(gpioDTO.getCronTriggerOn());
            gpio.setCronTriggerOff(gpioDTO.getCronTriggerOff());
            gpio.setDevice(device);
            gpioRepository.save(gpio);

            ScheduleGPIO scheduleGPIO = new ScheduleGPIO(gpio.getId(), gpio.getGpio(), device.getIp().getHostAddress(),
                    gpio.getCronTriggerOn(), gpio.getCronTriggerOff(), true);
            scheduleGPIOController.build(scheduleGPIO);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gpio, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a gpio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "gpio updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GPIO.class)) }),
            @ApiResponse(responseCode = "404", description = "Bad request", content = @Content) })
    @PutMapping(path = "/{id}")
    public ResponseEntity<GPIO> update(@Parameter(description = "Name of GPIO") @RequestBody @Valid GPIODTO gpioDTO)
            throws GPIONotFoundException {

        GPIO gpio = gpioRepository.findById(gpioDTO.getId()).orElseThrow(GPIONotFoundException::new);

        try {
            gpio.setMode(gpioDTO.getMode());
            gpio.setGpio(gpioDTO.getGpio());
            gpio.setCronTriggerOn(gpioDTO.getCronTriggerOn());
            gpio.setCronTriggerOff(gpioDTO.getCronTriggerOff());
            gpioRepository.save(gpio);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gpio, HttpStatus.OK);
    }

    @Operation(summary = "Get a device by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the GPIO", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GPIO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "GPIO not found", content = @Content) })
    @GetMapping("/{id}")
    public GPIODTO getById(@PathVariable Long id) {

        return gpioRepository.findByIdtoDTO(id);
    }

    @Operation(summary = "Get all GPIOs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found GPIOs", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GPIO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "GPIOs not found", content = @Content) })
    @GetMapping("/all")
    public @ResponseBody Iterable<GPIODTO> getAll() {

        return gpioRepository.findAlltoDTO();
    }

    @Operation(summary = "Get all GPIOs from device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found GPIOs", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GPIO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "GPIOs not found", content = @Content) })
    @GetMapping("/device/{deviceId}")
    public @ResponseBody Iterable<GPIODTO> getByDeviceId(@PathVariable Long deviceId) {

        return gpioRepository.findByDeviceIdtoDTO(deviceId);
    }
}
