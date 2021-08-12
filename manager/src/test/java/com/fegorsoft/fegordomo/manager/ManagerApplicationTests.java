package com.fegorsoft.fegordomo.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.fegorsoft.fegordomo.manager.controller.ScheduleGPIOController;
import com.fegorsoft.fegordomo.manager.dto.ScheduleGPIO;
import com.fegorsoft.fegordomo.manager.payload.ScheduleGPIOResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void addCronGPIO() throws UnknownHostException {
		ScheduleGPIO scheduleGPIO = new ScheduleGPIO();
		scheduleGPIO.setGpioId((long) 1);
		scheduleGPIO.setGpio((byte) 15);
		scheduleGPIO.setIp("192.168.1.101");
		scheduleGPIO.setCronTriggerOn("0/5 0 0 ? * * *");
		scheduleGPIO.setCronTriggerOff("0/6 0 0 ? * * *");

		ScheduleGPIOController s = new ScheduleGPIOController();
		ScheduleGPIOResponse res = s.build(scheduleGPIO);

		assertEquals("GPIO Scheduled Successfully!", res.getMessage());
	}
}
