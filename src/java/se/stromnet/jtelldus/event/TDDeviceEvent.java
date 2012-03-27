package se.stromnet.jtelldus.event;

import se.stromnet.jtelldus.Message;
import se.stromnet.jtelldus.Protocol.DeviceMethod;

public class TDDeviceEvent extends TelldusDeviceEvent {

	private DeviceMethod method; // or state..
	private String stateValue;

	public TDDeviceEvent(Message msg) {
		super("TDDeviceEvent", msg.takeInt());
		/* In callback code the method is called 'deviceState',
		 * In service code it is called eventState, which is always
		 * set to one of our DeviceMethod.* enums.
		 * Thus, using DeviceMethod here.
		 */ method = DeviceMethod.fromCode(msg.takeInt());
		stateValue = msg.takeString();
	}

	public DeviceMethod getMethod() {
		return method;
	}

	public String getStateValue() {
		return stateValue;
	}

	public interface Listener extends TelldusEvent.Listener {
		void onTDDeviceEvent(TDDeviceEvent event);
	}
}
