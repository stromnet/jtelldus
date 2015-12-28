package se.stromnet.jtelldus.event;

public abstract class TelldusDeviceEvent extends TelldusEvent {
	protected int deviceId;
	
	public TelldusDeviceEvent(String type, int deviceId) {
		super(type);
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "TelldusDeviceEvent{" +
				"deviceId=" + deviceId +
				'}';
	}

	public int getDeviceId() {
		return deviceId;
	}
}
