package se.stromnet.jtelldus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import se.stromnet.jtelldus.Protocol.ControllerType;
import se.stromnet.jtelldus.Protocol.DeviceMethod;
import se.stromnet.jtelldus.Protocol.DeviceType;
import se.stromnet.jtelldus.Protocol.ErrorCode;
import se.stromnet.jtelldus.event.TelldusEvent;

/**
 * Entry point for applications utilizing jtellduslib.
 * It provides all "application" level functionality, delegating the actual
 * work to an internal TelldusClient instance.
 *
 * The API tries to be as similar as possible to the original C libtelldus, at
 * least with regards to naming the tdXXX functions.
 *
 * The app should create one instance of this class. It will connect to
 * the appointed host/ports. Since telldusd has two seperate sockets,
 * two ports are required. clientPort is the port used for sending
 * explicit commands. eventPort is the port use to get event notifications.
 *
 * When created, a background notification thread will try to connect to the
 * appointed event port.
 * The client socket is only used when a explicit command is executed.
 *
 * Commands are executed by simply calling the tdXXX functions. They try to mimic
 * the regular C API in naming and parameters, but in some cases they have been
 * altered to suite a Java based implementation (such as lists).
 *
 * Notifications are delivered to the client app via Listeners. The client provides
 * a class which implements one ore more the TDxxxEvent.Listener interfaces.
 * For each interface implemented, the instance will have a function which is
 * called whenever an event arrives.
 * 
 * The events are dispatched in an ordered fashion, so first registered listener
 * will get it first. The event dispatching is synchronous, so make sure to not
 * perform any lengthy operations in the callback.
 *
 * @author Johan Ström <johan@stromnet.se>
 */
public class TelldusInterface {
	protected TelldusClient client;
	
	public TelldusInterface(String host, int clientPort, int eventPort) throws IOException {
		client = new TelldusClient(host, clientPort, eventPort);
	}

	public void close() {
		client.close();
	}

	/**
	 * Register a listener for a specific type of events.
	 * The type of listener is determined by the interface. All listeners
	 * are based on the TelldusEvent.Listener interface, but implementing that
	 * by it self will not accomplish anything. Instead, implement one or
	 * more of the TDxxxEvent.Listener interfaces.
	 *
	 * A listener can implement multiple interfaces. No lengthy operations
	 * should take place in the callbacks, they should return as soon as possible.
	 * 
	 * @param listener A instance of an object implementing a listener.
	 */
	public void registerListener(TelldusEvent.Listener listener) {
		client.registerEventListener(listener);
	}

	/**
	 * Unregister a previously registered event listener.
	 */
	public void unregisterListener(TelldusEvent.Listener listener) {
		client.unregisterEventListener(listener);
	}

	/**
	 * Turns a device on.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to turn on.
	 **/
	public int tdTurnOn(int deviceId) {
		Message m = new Message("tdTurnOn");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Turns a device off.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to turn off.
	 */
	public int tdTurnOff(int deviceId) {
		Message m = new Message("tdTurnOff");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Sends bell command to devices supporting this.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to send bell to
	 */
	public int tdBell(int deviceId) {
		Message m = new Message("tdBell");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Dims a device.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to dim
	 * @param level The level the device should dim to. This value should be 0-255
	 */
	public int tdDim(int deviceId, int level) {
		Message m = new Message("tdDim");
		m.addArgument(deviceId);
		m.addArgument(level);
		return client.getIntegerFromService(m);
	}

	/**
	 * Execute a scene action.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The id to execute
	 */
	public int tdExecute(int deviceId) {
		Message m = new Message("tdExecute");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Send "up" command to device.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to send the command to
	 */
	public int tdUp(int deviceId) {
		Message m = new Message("tdUp");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Send "down" command to device.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to send the command to
	 */
	public int tdDown(int deviceId) {
		Message m = new Message("tdDown");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Send "stop" command to device.
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to stop
	 */
	public int tdStop(int deviceId) {
		Message m = new Message("tdStop");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Sends a special learn command to some devices that need a special learn-command
	 * to be used from TellStick
	 * Make sure the device supports this by calling tdMethods() before any
	 * call to this function.
	 *
	 * @param intDeviceId The device id to learn.
	 */
	public int tdLearn(int deviceId) {
		Message m = new Message("tdLearn");
		m.addArgument(deviceId);
		return client.getIntegerFromService(m);
	}

	/**
	 * Returns the last sent command to a specific device
	 *
	 * @param intDeviceId The device id to query
	 * @param methodsSupported The methods supported by the client. See tdMethods() for more information.
	 *
	 * @return the last sent command as Protocol.DeviceMethod.
	 */
	public DeviceMethod tdLastSentCommand(int deviceId, int methodsSupported) {
		Message m = new Message("tdLastSentCommand");
		m.addArgument(deviceId);
		m.addArgument(methodsSupported);
		return DeviceMethod.fromCode(client.getIntegerFromService(m));
	}

	/**
	 * If the last sent command it TELLSTICK_DIM this returns the dimmed value.
	 *
	 * @param intDeviceId The device id to query
	 *
	 * @return the the value as a human readable string, example "128" for 50%
	 */
	public String tdLastSentValue(int deviceId) {
		Message m = new Message("tdLearn");
		m.addArgument(deviceId);

		return client.getStringFromService(m);
	}

	/**
	 * This function returns the number of devices configured
	 * @return an integer of the total number of devices configured
	 */
	public int tdGetNumberOfDevices() {
		return client.getIntegerFromService(new Message("tdGetNumberOfDevices"));
	}

	/**
	 * This function returns the unique id of a device with a specific index.
	 * To get all the id numbers you should loop over all the devices:
	 *
	 * int intNumberOfDevices = client.tdGetNumberOfDevices();
	 * for (int i = 0; i < intNumberOfDevices; i++) {
	 *   int id = client.tdGetDeviceId( i );
	 *   // id now contains the id number of the device with index of i
	 * }
	 *
	 * @param intDeviceIndex The device index to query. The index starts from 0.
	 *
	 * @return the unique id for the device or -1 if the device is not found.
	 */
	public int tdGetDeviceId(int intDeviceIndex) {
		Message msg = new Message("tdGetDeviceId");
		msg.addArgument(intDeviceIndex);
		return client.getIntegerFromService(msg);
	}

	/**
	 * Returns which type the device is. The device is one of Protocol.DeviceType
	 */
	public DeviceType tdGetDeviceType(int intDeviceId) {
		Message msg = new Message("tdGetDeviceType");
		msg.addArgument(intDeviceId);
		return DeviceType.fromCode(client.getIntegerFromService(msg));
	}

	/**
	 * Query a device for it's name.
	 * 
	 * @param intDeviceId The unique id of the device to query
	 * @return The name of the device or null if the device is not found.
	 */
	public String tdGetName(int intDeviceId) {
		Message msg = new Message("tdGetName");
		msg.addArgument(intDeviceId);
		return client.getStringFromService(msg);
	}

	/**
	 * Sets a new name for a device. The devices are global for all application, changing
	 * this will change the name for other applications as well.
	 * 
	 * @param intDeviceId The device id to change the name for
	 * @param strNewName The new name for the devices
	 * @return true on success, false otherwise.
	 */
	public boolean tdSetName(int intDeviceId, String strNewName) {
		Message msg = new Message("tdSetName");
		msg.addArgument(intDeviceId);
		msg.addArgument(strNewName);
		return client.getBoolFromService(msg);
	}

	/**
	 * @param intDeviceId The device id to query.
	 * @return the protocol used by a specific device.
	 */
	public String tdGetProtocol(int intDeviceId) {
		Message msg = new Message("tdGetProtocol");
		msg.addArgument(intDeviceId);
		return client.getStringFromService(msg);

	}

	/**
	 * This changes the current protocol used by a device. After changing the protocol,
	 * setting new parameters is required.
	 * 
	 * @param intDeviceId The device to change.
	 * @param strProtocol The new protocol to use.
	 * @return true on success, false otherwise.
	 */
	public boolean tdSetProtocol(int intDeviceId, String strProtocol) {
		Message msg = new Message("tdSetProtocol");
		msg.addArgument(intDeviceId);
		msg.addArgument(strProtocol);
		return client.getBoolFromService(msg);
	}

	/**
	 * @param intDeviceId The device to query.
	 * @return the model for a device. Not all protocols uses this.
	 */
	public String tdGetModel(int intDeviceId) {
		Message msg = new Message("tdGetModel");
		msg.addArgument(intDeviceId);
		return client.getStringFromService(msg);

	}

	/**
	 * Sets a new model for a device. Which model to set depends on the
	 * current protocol.
	 * 
	 * @param intDeviceId The device to change
	 * @param strModel The new model
	 * @return true on success, false otherwise.
	 */
	public boolean tdSetModel(int intDeviceId, String strModel) {
		Message msg = new Message("tdSetModel");
		msg.addArgument(intDeviceId);
		msg.addArgument(strModel);
		return client.getBoolFromService(msg);
	}

	/**
	 * Sets a new protocol specific parameter. Please see the documentation of the protocols
	 * before setting any parameter.
	 * 
	 * @param intDeviceId The device to change.
	 * @param strName The parameter to change.
	 * @param strValue The new value for the parameter.
	 * @return true on success, false otherwise.
	 */
	public boolean tdSetDeviceParameter(int intDeviceId, String strName, String strValue) {
		Message msg = new Message("tdSetDeviceParameter");
		msg.addArgument(intDeviceId);
		msg.addArgument(strName);
		msg.addArgument(strValue);
		return client.getBoolFromService(msg);
	}

	/**
	 * 
	 * @param intDeviceId The device to query.
	 * @param strName The name of the parameter to query.
	 * @param defaultValue A defaultValue to return if the current parameter hasn't previously been set.
	 * @return any protocol specific parameter specified by strName
	 */
	public String tdGetDeviceParameter(int intDeviceId, String strName, String defaultValue) {
		Message msg = new Message("tdGetDeviceParameter");
		msg.addArgument(intDeviceId);
		msg.addArgument(strName);
		msg.addArgument(defaultValue);
		return client.getStringFromService(msg);
	}

	/**
	 * Add a new device to the global database of devices.
	 * This function must be called first before any call to tdSetName(),
	 * tdSetProtocol() and similar functions.
	 * 
	 * @return The new device id for the newly created device. If the creation fails it returns a negative value.
	 */
	public int tdAddDevice() {
		Message msg = new Message("tdAddDevice");
		return client.getIntegerFromService(msg);
	}

	/**
	 * Removes a device.
	 * 
	 * @return true on success, false otherwise.
	 */
	public boolean tdRemoveDevice(int intDeviceId) {
		Message msg = new Message("tdRemoveDevice");
		msg.addArgument(intDeviceId);
		return client.getBoolFromService(msg);
	}

	/**
	 * Query a device for which methods it supports. By supplying the methods you support
	 * the library could remap the methods a device support for better fit the application.
	 *
	 * Example of querying a device supporting TELLSTICK_BELL:
	 * 
	 * int methods = tdMethods(id, DeviceMethod.TURNON, DeviceMethod.TURNOFF, DeviceMethod.BELL);
	 * //methods is now DeviceMethod.BELL.code()
	 * 
	 * int methods = tdMethods(id, DeviceMethod.TURNON, DeviceMethod.TURNOFF);
	 * //methods is now DeviceMethod.TURNOFF.code() because the client application doesn't support BELL
	 *
	 * // XXX JTelldus: Should clean this up with array of enum or something..
	 *
	 * @param id The device id to query
	 * @param methodsSupported The methods the client application supports
	 * 
	 * @return The method-flags Protocol.DeviceMethod codes OR'ed into an integer.
	 */
	public int tdMethods(int id, DeviceMethod... methodsSupported) {
		int m = 0;
		for(DeviceMethod dm: methodsSupported)
			m+= dm.code();

		Message msg = new Message("tdMethods");
		msg.addArgument(id);
		msg.addArgument(m);
		return client.getIntegerFromService(msg);
	}

	/**
	 * Get a human readable string from an error code returned
	 * from a function in telldus-core.
	 *
	 * @param intErrorNo The error code to translate.
	 * 
	 * @return a string ready to show to the user.
	 * @sa TELLSTICK_SUCCESS
	 * @sa TELLSTICK_ERROR_NOT_FOUND
	 * @sa TELLSTICK_ERROR_PERMISSION_DENIED
	 * @sa TELLSTICK_ERROR_DEVICE_NOT_FOUND
	 * @sa TELLSTICK_ERROR_METHOD_NOT_SUPPORTED
	 * @sa TELLSTICK_ERROR_UNKNOWN
	 */
	public String tdGetErrorString(int intErrorNo) {
		ErrorCode c = ErrorCode.fromCode(intErrorNo);
		return c.str();
	}
	
	/**
	 * Send a raw command to TellStick. Please read the TellStick protocol
	 * definition on how the command should be constructed.
	 * 
	 * @param command The command for TellStick in its native format
	 * 
	 * @return ErrorCode.SUCCESS on success or one of the other ErrorCodes on failure
	 */
	public ErrorCode tdSendRawCommand(String command, int reserved) {
		Message msg = new Message("tdSendRawCommand");
		msg.addArgument(command);
		msg.addArgument(reserved);
		return ErrorCode.fromCode(client.getIntegerFromService(msg));
	}

	public void tdConnectTellStickController(int vid, int pid, String serial) {
		Message msg = new Message("tdConnectTellStickController");
		msg.addArgument(vid);
		msg.addArgument(pid);
		msg.addArgument(serial);
		client.getStringFromService(msg);
	}

	public void tdDisconnectTellStickController(int vid, int pid, String serial) {
		Message msg = new Message("tdDisconnectTellStickController");
		msg.addArgument(vid);
		msg.addArgument(pid);
		msg.addArgument(serial);
		client.getStringFromService(msg);
	}


	/**
	 * Returns an list with all sensors available.
	 *
	 */
	public List<Sensor> tdSensor() {
		Message msg = new Message("tdSensor");
		String retval = client.getStringFromService(msg);
		if(retval == null || retval.length() == 0)
			return null;

		Message ret = Message.fromString(retval);
		int count = ret.takeInt();
		if(count == 0)
			return Collections.EMPTY_LIST;

		List<Sensor> l = new ArrayList<Sensor>(count);
		for(int i = 0; i < count; i++) {
			Sensor s = new Sensor(ret);
			l.add(s);
		}

		return l;
	}

	/**
	 * Get one of the supported sensor values from a sensor. Make sure it support
	 * the value type first by calling tdSensor(). The triplet protocol,
	 * model, and id together identifies a sensor.
	 *
	 * Added in version 2.1.0.
	 * @param protocol The protocol for the sensor
	 * @param model The model for the sensor
	 * @param id The id of the sensor
	 * @param dataType One of the datatype to retrieve
	 * @param value A SensorValue instance where we put the result.
	 *
	 * @returns ErrroCode.SUCCESS if the value could be fetched or one of the
	 *	errorcodes on failure
	 */
	public ErrorCode tdSensorValue(String protocol, String model, int id, int dataType, SensorValue value) {
		assert value != null;
		Message msg = new Message("tdSensorValue");
		msg.addArgument(protocol);
		msg.addArgument(model);
		msg.addArgument(id);
		msg.addArgument(dataType);

		String retval = client.getStringFromService(msg);
		if(retval == null || retval.length() == 0)
			return ErrorCode.ERROR_METHOD_NOT_SUPPORTED;

		Message ret = Message.fromString(retval);

		value.load(ret);
		
		return ErrorCode.SUCCESS;
	}


	/**
	 * Get a list of all controllers.
	 */
	public List<Controller> tdController() {
		Message msg = new Message("tdController");
		String retval = client.getStringFromService(msg);
		if(retval == null || retval.length() == 0)
			return null;

		Message ret = Message.fromString(retval);
		int count = ret.takeInt();
		if(count == 0)
			return Collections.EMPTY_LIST;

		List<Controller> l = new ArrayList<Controller>(count);
		for(int i = 0; i < count; i++) {
			Controller s = new Controller(ret);
			l.add(s);
		}

		return l;
	}
	
	public static class Controller {
		private int id;
		private ControllerType type;
		private String name;
		private boolean available;

		public Controller(Message src) {
			id = src.takeInt();
			type = ControllerType.fromCode(src.takeInt());
			name = src.takeString();
			available = (src.takeInt() == 1);
		}

		public int getId() {
			return id;
		}

		public ControllerType getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public boolean isAvailable() {
			return available;
		}
	}

	/**
	 * This function gets a parameter on a controller.
	 * Valid parameters are: serial and firmware
	 *
	 * @param controllerId The controller to change
	 * @param name The parameter to get.
	 * @return String with result, or null if not supported.
	 **/
	public String tdControllerValue(int controllerId, String name) {
		Message msg = new Message("tdControllerValue");
		msg.addArgument(controllerId);
		msg.addArgument(name);

		return client.getStringFromService(msg);
	}

	/**
	 * This function sets a parameter on a controller.
	 * Valid parameters are: name
	 *
	 * @param controllerId The controller to change
	 * @param name The parameter to change.
	 * @param value The new value for the parameter.
	 */
	public ErrorCode tdSetControllerValue(int controllerId, String name, String value) {
		Message msg = new Message("tdSetControllerValue");
		msg.addArgument(controllerId);
		msg.addArgument(name);
		msg.addArgument(value);
		return ErrorCode.fromCode(client.getIntegerFromService(msg));
	}

	/**
	 * This function removes a controller from the list	of controllers.
	 * The controller must not be available (disconnected) for this to work.
	 *
	 * @param controllerId The controller to remove
	 * @return SUCCESS if the controller was
	 * removed, ERROR_NOT_FOUND if the controller was
	 * not found, and ERROR_PERMISSION_DENIED if the
	 * controller is still connected.
	 */
	public ErrorCode tdRemoveController(int controllerId) {
		Message msg = new Message("tdRemoveController");
		msg.addArgument(controllerId);
		return ErrorCode.fromCode(client.getIntegerFromService(msg));
	}
}
