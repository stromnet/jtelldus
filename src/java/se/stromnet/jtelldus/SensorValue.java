package se.stromnet.jtelldus;


/**
 * Represents a SensorValue as returned from TelldusInterface.tdSensorValue,
 * and also used in TDSensorEvent.
 *
 * @author Johan Ström <johan@stromnet.se>
 */
public class SensorValue
{
	private String value;
	private int timestamp;

	/**
	 * Creates a blank sensor value,
	 * load later by calling load(Message).
	 */
	public SensorValue() {
	}

	/**
	 * Creates a new sensor value, load from message.
	 *
	 * @param msg Data to load
	 */
	public SensorValue(Message msg) {
		load(msg);
	}

	/**
	 * Updates data from message. Assumes message has fields, in order:
	 *
	 *	String value
	 *	Int timestamp
	 *
	 * This holds true for both tdSensorValue call and TDSensorEvent.
	 *
	 * @param src Message with fields according to above.
	 */
	public void load(Message msg) {
		value = msg.takeString();
		timestamp = msg.takeInt();
	}

	public String getValue() {
		return value;
	}

	public int getTimestamp() {
		return timestamp;
	}
}
