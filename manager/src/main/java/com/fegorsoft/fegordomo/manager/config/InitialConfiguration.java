package com.fegorsoft.fegordomo.manager.config;

import javax.annotation.PostConstruct;

import com.fegorsoft.fegordomo.manager.messages.MQTTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialConfiguration {
    
    @Autowired
    private MQTTService mqttService;
    
    @PostConstruct
    public void postConstruct() {
        mqttService.connect();

    }
    
}
