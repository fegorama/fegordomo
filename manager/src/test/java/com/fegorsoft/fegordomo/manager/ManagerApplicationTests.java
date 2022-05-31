package com.fegorsoft.fegordomo.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import com.fegorsoft.fegordomo.manager.controller.ScheduleOperationController;
import com.fegorsoft.fegordomo.manager.dto.ScheduleOperation;
import com.fegorsoft.fegordomo.manager.payload.ScheduleOperationResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void addCronOperation() throws UnknownHostException {
		ScheduleOperation scheduleOperation = new ScheduleOperation();
		scheduleOperation.setOperationId((long) 1);
		scheduleOperation.setDeviceName("Depuradora");
		scheduleOperation.setData("Test");
		scheduleOperation.setCronTriggerOn("0/5 0 0 ? * * *");
		scheduleOperation.setCronTriggerOff("0/6 0 0 ? * * *");

		ScheduleOperationController s = new ScheduleOperationController();
		ScheduleOperationResponse res = s.build(scheduleOperation);

		assertEquals("Operation Scheduled Successfully!", res.getMessage());
	}
}
