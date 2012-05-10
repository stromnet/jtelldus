package se.stromnet.jtelldus.event;

import se.stromnet.jtelldus.Message;
import se.stromnet.jtelldus.Protocol.ChangeEvent;
import se.stromnet.jtelldus.Protocol.ChangeType;

public class TDControllerEvent extends TelldusEvent {

	private int controllerId;
	private ChangeEvent changeEvent;
	private ChangeType changeType;
	private String newValue;

	public TDControllerEvent(Message msg) {
		super("TDControllerEvent");
		controllerId = msg.takeInt();
		changeEvent = ChangeEvent.fromCode(msg.takeInt());
		changeType = ChangeType.fromCode(msg.takeInt());
		newValue = msg.takeString();
	}

	public int getControllerId() {
		return controllerId;
	}

	public ChangeEvent getChangeEvent() {
		return changeEvent;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public String getNewValue() {
		return newValue;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+
						"[controllerId="+controllerId+", changeEvent="+changeEvent+
						 ", changeType="+changeType+", newValue="+newValue+"]";
	}

	public interface Listener extends TelldusEvent.Listener {
		void onTDControllerEvent(TDControllerEvent event);
	}
}
