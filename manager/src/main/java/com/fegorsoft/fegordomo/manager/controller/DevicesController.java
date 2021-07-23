package com.fegorsoft.fegordomo.manager.controller;

import com.fegorsoft.fegordomo.manager.model.Device;
import com.fegorsoft.fegordomo.manager.repository.DeviceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/devices")
public class DevicesController {

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewDevice (@RequestParam String name, @RequestParam String type) {
        Device device = new Device();
        device.setName(name);
        device.setType(type);
        deviceRepository.save(device);
        return "OK";
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
