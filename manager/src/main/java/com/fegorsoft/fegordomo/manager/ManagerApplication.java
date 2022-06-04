package com.fegorsoft.fegordomo.manager;

import java.util.Properties;

import javax.annotation.PreDestroy;

import com.fegorsoft.fegordomo.manager.messages.MQTTService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;

@SpringBootApplication
public class ManagerApplication {
	private static final Logger log = LoggerFactory.getLogger(ManagerApplication.class);

	@Autowired
	private MQTTService mqttService;

	private Server mqttBroker;

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			Properties properties = new Properties();
			MemoryConfig config = new MemoryConfig(properties);
			mqttBroker = new Server();
			mqttBroker.startServer(config);
			Runtime.getRuntime().addShutdownHook(new Thread(mqttBroker::stopServer));

			
			// Local client for MQTT
			mqttService.connect();
		};
	}

	@PreDestroy
	public void onExit() {
		log.info("Stopping broker");

		try {
			mqttBroker.stopServer();
			Thread.sleep(5 * 500);

		} catch (InterruptedException e) {
			log.error("", e);
		}

		log.info("Broker stopped");
	}

}
