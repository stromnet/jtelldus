package se.stromnet.jtelldus;

/**
 * Telldus protocol enums.
 *
 * This should cover all int datatypes sent by telldusd.
 *
 * @author johan
 */
public class Protocol {
	
	public enum DeviceMethod {
		TURNON(1),
		TURNOFF(2),
		BELL(4),
		TOGGLE(8),
		DIM(16),
		LEARN(32),
		EXECUTE(64),
		UP(128),
		DOWN(256),
		STOP(512);

		
		private int code;
		private DeviceMethod(int code) { this.code = code; }
		public int code() { return code; }

		public static DeviceMethod fromCode(int code) {
			for (DeviceMethod dm : DeviceMethod.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown device method " +code);
		}
	}

	public enum SensorValueType {
		TEMPERATURE(1, ""),
		HUMIDITY(2, "%"),
		RAINRATE(4, "mm/h"),
		RAINTOTAL(8, "mm"),
		WINDDIRECTION(16, ""),
		WINDAVERAGE(32, "m/s"),
		WINDGUST(64, "m/s");
		private int code;
		private String unit;

		private SensorValueType(int code, String unit) {
			this.code = code;
			this.unit = unit;
		}
		public int code() { return code; }

		public String unit() {
			return unit;
		}

		public static SensorValueType fromCode(int code) {
			for (SensorValueType dm : SensorValueType.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown sensor value type " +code);
		}

	}

	public enum ErrorCode {
		SUCCESS(0, "Success"),
		ERROR_NOT_FOUND(-1, "TellStick not found"),
		ERROR_PERMISSION_DENIED(-2, "Permission denied"),
		ERROR_DEVICE_NOT_FOUND(-3, "Device not found"),
		ERROR_METHOD_NOT_SUPPORTED(-4, "The method you tried to use is not supported by the device"),
		ERROR_COMMUNICATION(-5, "An error occurred while communicating with TellStick"),
		ERROR_CONNECTING_SERVICE(-6, "Could not connect to the Telldus Service"),
		ERROR_UNKNOWN_RESPONSE(-7, "Received an unknown response"),
		ERROR_SYNTAX(-8, "Input/command could not be parsed or didn't follow input rules"),
		ERROR_BROKEN_PIPE(-9, "Pipe broken during communication."),
		ERROR_COMMUNICATING_SERVICE(-10, "Timeout waiting for response from the Telldus Service."),
		ERROR_UNKNOWN(-99, "Unknown");

		private int code;
		private String str;
		private ErrorCode(int code, String str) { this.code = code; this.str = str; }
		public int code() { return code; }
		public String str() { return str; }

		public static ErrorCode fromCode(int code) {
			for (ErrorCode dm : ErrorCode.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown error code " +code);
		}
	}

	public enum DeviceType {
		TYPE_DEVICE(1),
		TYPE_GROUP(2),
		TYPE_SCENE(3);

		private int code;
		private DeviceType(int code) { this.code = code; }
		public int code() { return code; }

		public static DeviceType fromCode(int code) {
			for (DeviceType dm : DeviceType.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown device type " +code);
		}
	}


	public enum ControllerType {
		TELLSTICK(1),
		TELLSTICK_DUO(2),
		TELLSTICK_NET(3);

		private int code;
		private ControllerType(int code) { this.code = code; }
		public int code() { return code; }

		public static ControllerType fromCode(int code) {
			for (ControllerType dm : ControllerType.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown controller type " +code);
		}
	}
	

	public enum ChangeEvent {
		DEVICE_ADDED(1),
		DEVICE_CHANGED(2),
		DEVICE_REMOVED(3),
		DEVICE_STATE_CHANGED(4);

		private int code;
		private ChangeEvent(int code) { this.code = code; }
		public int code() { return code; }

		public static ChangeEvent fromCode(int code) {
			if(code == 0)
				return null;
			
			for (ChangeEvent dm : ChangeEvent.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown change event " +code);
		}
	}

	public enum ChangeType {
		CHANGE_NAME(1),
		CHANGE_PROTOCOL(2),
		CHANGE_MODEL(3),
		CHANGE_METHOD(4),
		CHANGE_AVAILABLE(5),
		CHANGE_FIRMWARE(6);

		private int code;
		private ChangeType(int code) { this.code = code; }
		public int code() { return code; }

		public static ChangeType fromCode(int code) {
			if(code == 0)
				return null;
			
			for (ChangeType dm : ChangeType.values()) {
				if (dm.code() == code)
					return dm;
			}
			throw new IllegalArgumentException("Unknown change type " +code);
		}
	}
}
