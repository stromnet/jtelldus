package se.stromnet.jtelldus;


/**
 * Represents a SensorValue as returned from TelldusInterface.tdSensorValue,
 * and also used in TDSensorEvent.
 *
 * @author Johan Ström <johan@stromnet.se>
 */
public class SensorValue
{
	private Protocol.SensorValueType dataType;
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

	public Protocol.SensorValueType getDataType() {
		return dataType;
	}

	/**
	 * Updates data from message. Assumes message has fields, in order:
	 *
	 *	String value

	 *	Int timestamp
	 *
	 * This holds true for both tdSensorValue call and TDSensorEvent.
	 *
	 * @param msg Message with fields according to above.
	 */
	public void load(Message msg) {
		int dt = msg.takeInt();
		dataType = Protocol.SensorValueType.fromCode(dt);
		value = msg.takeString();
		timestamp = msg.takeInt();
		switch (dataType) {
			case WINDDIRECTION:
				int direction = Integer.parseInt(value);
				switch (direction) {
					case 0:
						value = "N";
						break;
					case 1:
						value = "NNE";
						break;
					case 2:
						value = "NE";
						break;
					case 3:
						value = "ENE";
						break;
					case 4:
						value = "E";
						break;
					case 5:
						value = "ESE";
						break;
					case 6:
						value = "SE";
						break;
					case 7:
						value = "SSE";
						break;
					case 8:
						value = "S";
						break;
					case 9:
						value = "SSW";
						break;
					case 10:
						value = "SW";
						break;
					case 11:
						value = "WSW";
						break;
					case 12:
						value = "W";
						break;
					case 13:
						value = "WNW";
						break;
					case 14:
						value = "NW";
						break;
					case 15:
						value = "NNW";
						break;
				}
				break;

		}
	}

	public String getValue() {
		return value;
	}

	public int getTimestamp() {
		return timestamp;
	}


	@Override
	public String toString() {
		return "SensorValue{" +
				"dataType=" + dataType +
				", value='" + value + '\'' +
				", timestamp=" + timestamp +
				'}';
	}
}
