package se.stromnet.jtelldus;

import org.junit.Ignore;
import org.junit.Test;
import se.stromnet.jtelldus.Protocol.DeviceType;

/**
 *
 * @author johan
 */
public class TelldusClientTest {

    @Ignore
    @Test
    public void testReal() throws Exception {
		TelldusInterface td = new TelldusInterface("172.28.1.2", 9998, 9999);

		int devices = td.tdGetNumberOfDevices();
		
		System.out.println("Got "+devices+" connected.");

		for(int i = 0; i < devices; i++) {
			int id = td.tdGetDeviceId(i);
			String name  = td.tdGetName(id);
			DeviceType t = td.tdGetDeviceType(id);
			System.out.println(id+" : "+ name +" of type "+ t);
			
		}

		td.tdTurnOff(3);
		td.tdTurnOff(3);
		td.tdTurnOff(3);
		td.tdTurnOff(3);
		
		td.close();
	}

}
