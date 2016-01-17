package se.stromnet.jtelldus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromnet.jtelldus.event.TDControllerEvent;
import se.stromnet.jtelldus.event.TDDeviceEvent;
import se.stromnet.jtelldus.event.TDRawDeviceEvent;
import se.stromnet.jtelldus.event.TDSensorEvent;

import java.io.IOException;

/**
 * Created by ripl1 on 2015-02-02.
 */
public class ReadEventsExample implements TDSensorEvent.Listener, TDControllerEvent.Listener, TDRawDeviceEvent.Listener, TDDeviceEvent.Listener {
    private static final Logger log = LoggerFactory.getLogger(TelldusClient.class);
    private TelldusInterface client;

    public ReadEventsExample(String host, int clientPort, int eventPort) throws IOException {
        client = new TelldusInterface(host, clientPort, eventPort);
        client.registerListener(this);
    }

    public static void main(String[] args) {
        try {
            ReadEventsExample re = new ReadEventsExample(args[0], 9998, 9999);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onTDRawDeviceEvent(TDRawDeviceEvent event) {
        log.info(event.toString());
    }

    public void onTDSensorEvent(TDSensorEvent event) {
        log.info(event.getSensor() + " " + event.getSensorValue());
    }

    public void onTDControllerEvent(TDControllerEvent event) {
        log.debug(event.toString());
    }

    public void onTDDeviceEvent(TDDeviceEvent event) {
        log.info(event.toString());
    }
}

