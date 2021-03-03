package hu.bme.mit.train.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.system.TrainSystem;

import java.time.LocalDateTime;

public class TrainSystemTest {

	TrainController controller;
	TrainSensor sensor;
	TrainUser user;
	TrainSystem system;
	
	@Before
	public void before() {
		system = new TrainSystem();
		controller = system.getController();
		sensor = system.getSensor();
		user = system.getUser();

		sensor.overrideSpeedLimit(50);
	}
	
	@Test
	public void OverridingJoystickPosition_IncreasesReferenceSpeed() {
		sensor.overrideSpeedLimit(10);

		Assert.assertEquals(0, controller.getReferenceSpeed());
		
		user.overrideJoystickPosition(5);

		controller.followSpeed();
		Assert.assertEquals(5, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());
	}

	@Test
	public void OverridingJoystickPositionToNegative_SetsReferenceSpeedToZero() {
		user.overrideJoystickPosition(4);
		controller.followSpeed();
		user.overrideJoystickPosition(-5);
		controller.followSpeed();
		Assert.assertEquals(0, controller.getReferenceSpeed());
	}

	@Test //Testing if larger speed limit makes the train go faster
	public void newTest1(){
		sensor.overrideSpeedLimit(10);
		user.overrideJoystickPosition(10);

		controller.followSpeed();
		Assert.assertEquals(10, controller.getReferenceSpeed());

		sensor.overrideSpeedLimit(20);

		controller.followSpeed();
		Assert.assertEquals(20, controller.getReferenceSpeed());

	}

	@Test //Testing tachograph
	public void tachographTest(){
		system.getTachograph().put(LocalDateTime.now(), user.getJoystickPosition(), controller.getReferenceSpeed());

		Assert.assertFalse(system.getTachograph().isEmpty());
	}
}
