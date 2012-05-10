package se.stromnet.jtelldus.event;

public abstract class TelldusEvent {

	protected String type;

	public TelldusEvent(String type) {
		this.type = type;
	}

	public interface Listener {
		/* Dummy base for all listeners */
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[]";
	}
}
