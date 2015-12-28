package se.stromnet.jtelldus;

/**
 * Represents a Sensor as returned from TelldusInterface.tdSensor,
 * and also used in TDSensorEvent.
 *
 * @author Johan Ström <johan@stromnet.se>
 */
public class Sensor
{
	private String protocol;
	private String model;
	private int id;

	/**
	 * Construct a Sensor from message. Assumes message has fields, in order:
	 *
	 *	String protocol
	 *	String model
	 *	Int id
	 *	Int dataTypes
	 *
	 * This holds true for both tdSensor call and TDSensorEvent.
	 *
	 * @param src Message with fields according to above.
	 */
	public Sensor(Message src) {
		protocol = src.takeString();
		model = src.takeString();
		id = src.takeInt();
	}

	public String getProtocol() {
		return protocol;
	}

	public String getModel() {
		return model;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Sensor{" +
				"protocol='" + protocol + '\'' +
				", model='" + model + '\'' +
				", id=" + id +
				'}';
	}
}
