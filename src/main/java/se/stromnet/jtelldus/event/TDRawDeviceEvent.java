package se.stromnet.jtelldus.event;

import se.stromnet.jtelldus.Message;

public class TDRawDeviceEvent extends TelldusEvent {

	private String data;
	private int controllerId;

	public TDRawDeviceEvent(Message msg) {
		super("TDRawDeviceEvent");
		data = msg.takeString();
		controllerId = msg.takeInt();
	}

	public String getData() {
		return data;
	}

	public int getControllerId() {
		return controllerId;
	}

	@Override
	public String toString() {
		return "TDRawDeviceEvent{" +
				"data='" + data + '\'' +
				", controllerId=" + controllerId +
				'}';
	}

	public interface Listener extends TelldusEvent.Listener {
		void onTDRawDeviceEvent(TDRawDeviceEvent event);
	}
}
