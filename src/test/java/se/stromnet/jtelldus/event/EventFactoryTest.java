package se.stromnet.jtelldus.event;

import org.junit.Test;
import se.stromnet.jtelldus.Message;
import se.stromnet.jtelldus.Protocol.ChangeEvent;
import se.stromnet.jtelldus.Protocol.ChangeType;
import se.stromnet.jtelldus.Protocol.DeviceMethod;
import se.stromnet.jtelldus.Protocol.SensorValueType;

import static org.junit.Assert.*;

/**
 *
 * @author johan
 */
public class EventFactoryTest {

	@Test
	public void testTDDeviceChangeEvent() {
		Message msg = new Message();

		msg.addArgument("TDDeviceChangeEvent");
		msg.addArgument(1); // Device 1
		msg.addArgument(ChangeEvent.DEVICE_ADDED.code());
		msg.addArgument(ChangeType.CHANGE_AVAILABLE.code());

		msg = new Message(msg.serialize());

		TDDeviceChangeEvent e = (TDDeviceChangeEvent) EventFactory.createEvent(msg);

		assertNotNull(e);
		assertEquals(1, e.getDeviceId());
		assertEquals(ChangeEvent.DEVICE_ADDED, e.getChangeEvent());
		assertEquals(ChangeType.CHANGE_AVAILABLE, e.getChangeType());
	}


	@Test
	public void testTDDeviceEvent() {
		Message msg = new Message();

		msg.addArgument("TDDeviceEvent");
		msg.addArgument(1); // Device 1
		msg.addArgument(DeviceMethod.TURNOFF.code());
		msg.addArgument("");

		msg = new Message(msg.serialize());

		TDDeviceEvent e = (TDDeviceEvent) EventFactory.createEvent(msg);

		assertNotNull(e);
		assertEquals(1, e.getDeviceId());
		assertEquals(DeviceMethod.TURNOFF, e.getMethod());
		assertEquals("", e.getStateValue());
	}

	@Test
	public void testTDRawDeviceEvent() {
		Message msg = new Message();

		msg.addArgument("TDRawDeviceEvent");
		msg.addArgument("SomeData");
		msg.addArgument(1); // Controller ID
		
		msg = new Message(msg.serialize());

		TDRawDeviceEvent e = (TDRawDeviceEvent) EventFactory.createEvent(msg);

		assertNotNull(e);
		assertEquals("SomeData", e.getData());
		assertEquals(1, e.getControllerId());
	}


	@Test
	public void testHumidityTDSensorEvent() {
		Message msg = new Message();

		msg.addArgument("TDSensorEvent");
		msg.addArgument("someProtocol");
		msg.addArgument("someModel");
		msg.addArgument(123);
		msg.addArgument(SensorValueType.HUMIDITY.code());
		msg.addArgument("80");
		msg.addArgument(123441242); // XXX: This is really sent as C int; ugly casted from time_t...

		msg = new Message(msg.serialize());

		TDSensorEvent e = (TDSensorEvent) EventFactory.createEvent(msg);

		assertNotNull(e);
		assertEquals("someProtocol", e.getSensor().getProtocol());
		assertEquals("someModel", e.getSensor().getModel());
		assertEquals(123, e.getSensor().getId());
		assertEquals(SensorValueType.HUMIDITY, e.getSensorValue().getDataType());
		assertEquals("80", e.getSensorValue().getValue());
		assertEquals(123441242, e.getSensorValue().getTimestamp());
	}

	@Test
	public void testWinddirectionTDSensorEvent() {
		Message msg = new Message();

		msg.addArgument("TDSensorEvent");
		msg.addArgument("someProtocol");
		msg.addArgument("someModel");
		msg.addArgument(123);
		msg.addArgument(SensorValueType.WINDDIRECTION.code());
		msg.addArgument("15");
		msg.addArgument(123441242); // XXX: This is really sent as C int; ugly casted from time_t...

		msg = new Message(msg.serialize());

		TDSensorEvent e = (TDSensorEvent) EventFactory.createEvent(msg);

		assertNotNull(e);
		assertEquals("someProtocol", e.getSensor().getProtocol());
		assertEquals("someModel", e.getSensor().getModel());
		assertEquals(123, e.getSensor().getId());
		assertEquals(SensorValueType.WINDDIRECTION, e.getSensorValue().getDataType());
		assertEquals("NNW", e.getSensorValue().getValue());
		assertEquals(123441242, e.getSensorValue().getTimestamp());
	}


	@Test
	public void testTDControllerEvent() {
		Message msg = new Message();

		msg.addArgument("TDControllerEvent");
		msg.addArgument(3);
		msg.addArgument(ChangeEvent.DEVICE_ADDED.code());
		msg.addArgument(0);
		msg.addArgument("");
		
		msg = new Message(msg.serialize());

		TDControllerEvent e = (TDControllerEvent) EventFactory.createEvent(msg);

		assertNotNull(e);
		assertEquals(3, e.getControllerId());
		assertEquals(ChangeEvent.DEVICE_ADDED, e.getChangeEvent());
		assertNull(e.getChangeType());
		assertEquals("", e.getNewValue());
	}

}
