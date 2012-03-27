package se.stromnet.jtelldus.event;

import se.stromnet.jtelldus.Message;
import se.stromnet.jtelldus.Protocol.ChangeEvent;
import se.stromnet.jtelldus.Protocol.ChangeType;

public class TDDeviceChangeEvent extends TelldusDeviceEvent {

	private ChangeEvent changeEvent;
	private ChangeType changeType;

	public TDDeviceChangeEvent(Message msg) {
		super("TDDeviceChangeEvent", msg.takeInt());
		/* In service code, changeEvent is called eventDeviceChanges, which would
		 * imply multiple changes (which doesn't make sense though, since values are
		 * not bit-weighted).
		 * However, in client callback code (CallbackDispatcher.h) it is called
		 * changeEvent, so thats what we use here.
		 */
		changeEvent = ChangeEvent.fromCode(msg.takeInt());
		changeType = ChangeType.fromCode(msg.takeInt());
	}

	public ChangeEvent getChangeEvent() {
		return changeEvent;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public interface Listener extends TelldusEvent.Listener {
		void onTDDeviceChangeEvent(TDDeviceChangeEvent event);
	}
}
