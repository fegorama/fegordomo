package com.fegorsoft.fegordomo.manager;

import java.util.Properties;

import com.fegorsoft.fegordomo.manager.messages.MQTTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
@SpringBootApplication
public class ManagerApplication {

	@Autowired
    private MQTTService mqttService;
	
	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			Properties properties = new Properties();
			MemoryConfig config = new MemoryConfig(properties);
			Server mqttBroker = new Server();
			mqttBroker.startServer(config);
			Runtime.getRuntime().addShutdownHook(new Thread(mqttBroker::stopServer));

			// Local client for MQTT
			mqttService.connect();
		};
	}
  
}
