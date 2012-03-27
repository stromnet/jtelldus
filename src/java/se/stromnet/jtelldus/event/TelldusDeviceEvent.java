package se.stromnet.jtelldus.event;

public abstract class TelldusDeviceEvent extends TelldusEvent {
	protected int deviceId;
	
	public TelldusDeviceEvent(String type, int deviceId) {
		super(type);
		this.deviceId = deviceId;
	}

	public int getDeviceId() {
		return deviceId;
	}
}
