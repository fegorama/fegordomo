package com.fegorsoft.fegordomo.manager;

import java.io.File;
import java.util.Properties;

import javax.annotation.PreDestroy;

import com.fegorsoft.fegordomo.manager.messages.MQTTService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import io.moquette.broker.Server;
import io.moquette.broker.config.FileResourceLoader;
import io.moquette.broker.config.IConfig;
import io.moquette.broker.config.IResourceLoader;
import io.moquette.broker.config.MemoryConfig;
import io.moquette.broker.config.ResourceLoaderConfig;

@SpringBootApplication
public class ManagerApplication {
	private static final Logger log = LoggerFactory.getLogger(ManagerApplication.class);

 	@Value("${moquette.config-file}")
    private String moquetteConfigFile;

	@Autowired
	private MQTTService mqttService;

	private Server mqttBroker;

	public static void main(String[] args) {
		log.info("Java Home: {}", System.getProperty("java.home"));
		log.info("Java Version: {}", System.getProperty("java.version"));
		log.info("Java Runtime Version: {}", System.getProperty("java.runtime.version"));

		SpringApplication.run(ManagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			//File file = ResourceUtils.getFile("classpath:conf/moquette.conf");
			File file = ResourceUtils.getFile(moquetteConfigFile);
			log.info("Moquette Path Config: {}", file.getAbsolutePath());
			IResourceLoader filesystemLoader = new FileResourceLoader(file);
			final IConfig config = new ResourceLoaderConfig(filesystemLoader);
			//Properties properties = new Properties();
			//MemoryConfig config = new MemoryConfig(properties); 
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
