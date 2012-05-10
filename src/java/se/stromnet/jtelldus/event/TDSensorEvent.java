package se.stromnet.jtelldus.event;

import se.stromnet.jtelldus.Message;
import se.stromnet.jtelldus.Sensor;
import se.stromnet.jtelldus.SensorValue;

public class TDSensorEvent extends TelldusEvent {

	private Sensor sensor;
	private SensorValue value;

	public TDSensorEvent(Message msg) {
		super("TDSensorEvent");
		sensor = new Sensor(msg);
		value = new SensorValue(msg);
	}

	public Sensor getSensor() {
		return sensor;
	}

	public SensorValue getSensorValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[sensor="+sensor+", value="+value+"]";
	}

	public interface Listener extends TelldusEvent.Listener {
		void onTDSensorEvent(TDSensorEvent event);
	}
}
