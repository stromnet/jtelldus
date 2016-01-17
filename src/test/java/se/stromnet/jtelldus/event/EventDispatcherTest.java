package se.stromnet.jtelldus.event;

import org.junit.Before;
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
public class EventDispatcherTest {

	private EventDispatcher ed;

	@Before
	public void setUp() {
		ed = new EventDispatcher();
	}


	private TDDeviceChangeEvent createTDDeviceChangeEvent() {
		Message msg = new Message();
		msg.addArgument(1); // Device 1
		msg.addArgument(ChangeEvent.DEVICE_ADDED.code());
		msg.addArgument(ChangeType.CHANGE_AVAILABLE.code());
		msg = new Message(msg.serialize());
		
		return new TDDeviceChangeEvent(msg);
	}

	private TDDeviceEvent createTDDeviceEvent() {
		Message msg = new Message();
		msg.addArgument(1); // Device 1
		msg.addArgument(DeviceMethod.DOWN.code());
		msg.addArgument("dummyvalue");
		msg = new Message(msg.serialize());

		return new TDDeviceEvent(msg);
	}

	private TDSensorEvent createTDSensorEvent() {
		Message msg = new Message();
		msg.addArgument("someProtocol");
		msg.addArgument("someModel");
		msg.addArgument(123);
		msg.addArgument(SensorValueType.HUMIDITY.code());
		msg.addArgument("80%");
		msg.addArgument(123441242);
		msg = new Message(msg.serialize());

		return new TDSensorEvent(msg);
	}


	@Test
	public void testDispatcher() {

		TDDeviceChangeEvent dce = createTDDeviceChangeEvent();
		TDDeviceEvent de = createTDDeviceEvent();
		TDSensorEvent se = createTDSensorEvent();

		// Dispatch it to nobody
		ed.dispatchEvent(dce);
		ed.dispatchEvent(de);
		ed.dispatchEvent(se);

		// add two listeners, one matching and one not
		TDDeviceChangeEventListener listener = new TDDeviceChangeEventListener();
		TDDeviceEventListener listener2 = new TDDeviceEventListener();
		assertNull(listener.event);
		assertNull(listener2.event);
		ed.addEventListener(listener);
		ed.addEventListener(listener2);

		// dispatch the first
		ed.dispatchEvent(dce);

		// Verify
		assertNotNull(listener.event);
		assertEquals(1, listener.event.getDeviceId());
		assertEquals(ChangeEvent.DEVICE_ADDED, listener.event.getChangeEvent());
		assertEquals(ChangeType.CHANGE_AVAILABLE, listener.event.getChangeType());

		// Verify second did not get
		assertNull(listener2.event);
		
		listener.reset();
		listener2.reset();



		// dispatch the second
		ed.dispatchEvent(de);

		// Verify first listener did not get
		assertNull(listener.event);

		// Verify second got it
		assertNotNull(listener2.event);
		assertEquals(1, listener2.event.getDeviceId());
		assertEquals(DeviceMethod.DOWN, listener2.event.getMethod());
		assertEquals("dummyvalue", listener2.event.getStateValue());

		listener.reset();
		listener2.reset();

		// Dispatch third sensor event, no one gets it.
		ed.dispatchEvent(se);
		assertNull(listener.event);
		assertNull(listener2.event);

		// Remove listener 2, make sure it does not get more events
		ed.removeEventListener(listener2);

		// dispatch the second again; listener2 would have received it
		ed.dispatchEvent(de);
		assertNull(listener.event);
		assertNull(listener2.event);
	}

	@Test
	public void testMultiListener() {
		TDDeviceAndSensorEventListener listener = new TDDeviceAndSensorEventListener();

		TDDeviceEvent de = createTDDeviceEvent();
		TDSensorEvent se = createTDSensorEvent();

		ed.addEventListener(listener);

		ed.dispatchEvent(de);

		assertNotNull(listener.deviceEvent);
		assertNull(listener.sensorEvent);
		
		ed.dispatchEvent(se);

		assertNotNull(listener.deviceEvent);
		assertNotNull(listener.sensorEvent);
	}



	/* Dummy listeners */

	private static class TDDeviceChangeEventListener implements TDDeviceChangeEvent.Listener {
		public TDDeviceChangeEvent event;
		public void onTDDeviceChangeEvent(TDDeviceChangeEvent event) {
			this.event = event;
		}
		public void reset() {
			event = null;
		}
	}
	private static class TDDeviceEventListener implements TDDeviceEvent.Listener {
		public TDDeviceEvent event;
		public void onTDDeviceEvent(TDDeviceEvent event) {
			this.event = event;
		}
		public void reset() {
			event = null;
		}
	}


	private static class TDDeviceAndSensorEventListener
					implements TDDeviceEvent.Listener, TDSensorEvent.Listener {
		public TDDeviceEvent deviceEvent;
		public TDSensorEvent sensorEvent;
		
		public void onTDDeviceEvent(TDDeviceEvent event) {
			this.deviceEvent = event;
		}

		public void onTDSensorEvent(TDSensorEvent event) {
			this.sensorEvent = event;
		}
		public void reset() {
			deviceEvent = null;
			sensorEvent = null;
		}
	}
}
