package se.stromnet.jtelldus.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is an internal JTelldus class which provides the event dispatching
 * functionality.
 *
 * Event listeners are added, and for any interface it implements it will
 * get events for.
 *
 * @author johan
 */
public class EventDispatcher {

	private Map<Class<? extends TelldusEvent>,
					List<? extends TelldusEvent.Listener>> listeners;


	public EventDispatcher() {
		listeners = new HashMap<Class<? extends TelldusEvent>,
						List<? extends TelldusEvent.Listener>>();
	}

	public void dispatchEvent(TelldusEvent event) {
		List<? extends TelldusEvent.Listener>
						ll = listeners.get(event.getClass());

		// No listeners registerd for this class?
		if(ll == null)
			return;

		for(TelldusEvent.Listener l: ll) {
			// XXX: Can this be done nicer?
			if(event instanceof TDDeviceChangeEvent)
				((TDDeviceChangeEvent.Listener)l).onTDDeviceChangeEvent((TDDeviceChangeEvent) event);
			else if(event instanceof TDDeviceEvent)
				((TDDeviceEvent.Listener)l).onTDDeviceEvent((TDDeviceEvent) event);
			else if(event instanceof TDRawDeviceEvent)
				((TDRawDeviceEvent.Listener)l).onTDRawDeviceEvent((TDRawDeviceEvent) event);
			else if(event instanceof TDSensorEvent)
				((TDSensorEvent.Listener)l).onTDSensorEvent((TDSensorEvent) event);
			else if(event instanceof TDControllerEvent)
				((TDControllerEvent.Listener)l).onTDControllerEvent((TDControllerEvent) event);
		}
	}

	/**
	 * Add an event listener. The listener interfaces implemented decides
	 * what events will be listened for.
	 */
	public void addEventListener(TelldusEvent.Listener listener) {
		if(listener instanceof TDDeviceChangeEvent.Listener)
			addEventListener(TDDeviceChangeEvent.class, listener);

		if(listener instanceof TDDeviceEvent.Listener)
			addEventListener(TDDeviceEvent.class, listener);

		if(listener instanceof TDRawDeviceEvent.Listener)
			addEventListener(TDRawDeviceEvent.class, listener);

		if(listener instanceof TDSensorEvent.Listener)
			addEventListener(TDSensorEvent.class, listener);
		
		if(listener instanceof TDControllerEvent.Listener)
			addEventListener(TDControllerEvent.class, listener);
	}


	/**
	 * Remove an event listener.
	 * All instances in all classes will be removed.
	 */
	public void removeEventListener(TelldusEvent.Listener listener) {
		for(Class clazz: listeners.keySet()) {
			Iterator<? extends TelldusEvent.Listener> i = listeners.get(clazz).iterator();
			while(i.hasNext()) {
				if(i.next() == listener)
					i.remove();
			}
		}
	}

	/**
	 * Adds an event listener for the specific
	 * event class.
	 *
	 * Mainly for internal use.
	 */
	public void addEventListener(Class<? extends TelldusEvent> eventClass, TelldusEvent.Listener listener) {
		List ll = listeners.get(eventClass);
		if(ll == null)
			listeners.put(eventClass, ll = new LinkedList());

		ll.add(listener);
	}

}
