package se.stromnet.jtelldus;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author johan
 */
public class MessageTest {
	Message m;
	ByteBuffer b;
	@Test
	public void testBuild() {
		m = new Message();
		
	}


	@Test
	public void testParse() {
		b = ByteBuffer.allocate(1000);

		b.put("5:abcde".getBytes());
		b.flip(); // Switch to reading from buffer

		assertFalse(Message.nextIsInt(b));
		assertFalse(Message.nextIsInt(b));
		assertFalse(Message.nextIsInt(b));
		
		assertTrue(Message.nextIsString(b));
		assertTrue(Message.nextIsString(b));
		assertTrue(Message.nextIsString(b));
		assertEquals("abcde", Message.takeString(b));
		//assertFalse(b.hasRemaining());

		b.compact();
		b.put("i55s".getBytes());
		b.flip(); // Switch to reading from buffer

		assertTrue(Message.nextIsInt(b));
		assertTrue(Message.nextIsInt(b));
		assertTrue(Message.nextIsInt(b));
		assertTrue(Message.nextIsInt(b));
		assertFalse(Message.nextIsString(b));
		assertFalse(Message.nextIsString(b));
		assertFalse(Message.nextIsString(b));
		assertFalse(Message.nextIsString(b));
		assertEquals(55, Message.takeInt(b));
		//assertFalse(b.hasRemaining());

		b.compact();
		b.put("5:abcdei99999s2:OK".getBytes());
		b.flip(); // Switch to reading from buffer

		assertFalse(Message.nextIsInt(b));
		assertTrue(Message.nextIsString(b));
		assertEquals("abcde", Message.takeString(b));
		//assertEquals(11, b.limit() - b.position());

		assertTrue(Message.nextIsInt(b));
		assertFalse(Message.nextIsString(b));
		assertEquals(99999, Message.takeInt(b));
		//assertEquals(4, b.limit() - b.position());
		
		assertFalse(Message.nextIsInt(b));
		assertTrue(Message.nextIsString(b));
		assertEquals("OK", Message.takeString(b));
		//assertEquals(0, b.limit() - b.position());


		// Test some crap
		b.compact();
		b.put("abcd".getBytes());
		b.flip(); // Switch to reading from buffer

		assertFalse(Message.nextIsInt(b));
		assertFalse(Message.nextIsString(b));
		try {
			Message.takeInt(b);
			assertTrue(false);
		}catch(BufferUnderflowException ex) {}

		try {
			assertEquals(null, Message.takeString(b));
			assertTrue(false);
		}catch(BufferUnderflowException ex) {}
		
		b.clear();


		// Test some partial
		b.put("5:abcd".getBytes());
		b.flip(); // Switch to reading from buffer

		assertFalse(Message.nextIsInt(b));
		assertTrue(Message.nextIsString(b));
		try {
			assertEquals(null, Message.takeString(b));
			assertTrue(false);
		}catch(BufferUnderflowException ex) {}
		
		b.put("e".getBytes());
		b.flip(); // Switch to reading from buffer
		assertFalse(Message.nextIsInt(b));
		assertTrue(Message.nextIsString(b));
		assertEquals("abcde", Message.takeString(b));


		// Test some real
		b.compact();
		b.put("13:TDDeviceEventi1si1s1:013:TDDeviceEventi1si2s1:0".getBytes());
		b.flip(); // Switch to reading from buffer
		assertTrue(Message.nextIsString(b));
		assertEquals("TDDeviceEvent", Message.takeString(b));

		assertTrue(Message.nextIsInt(b));
		assertEquals(1, Message.takeInt(b));

		assertTrue(Message.nextIsInt(b));
		assertEquals(1, Message.takeInt(b));

		assertTrue(Message.nextIsString(b));
		assertEquals("0", Message.takeString(b));
		

		assertTrue(Message.nextIsString(b));
		assertEquals("TDDeviceEvent", Message.takeString(b));

		assertTrue(Message.nextIsInt(b));
		assertEquals(1, Message.takeInt(b));

		assertTrue(Message.nextIsInt(b));
		assertEquals(2, Message.takeInt(b));

		assertTrue(Message.nextIsString(b));
		assertEquals("0", Message.takeString(b));
	}

	@Test public void testBBConstructor() {
		b = ByteBuffer.allocate(1000);
		b.put("13:TDDeviceEventi1si1s1:0".getBytes());
		b.flip(); // Switch to reading from buffer


		m = new Message(b);
		assertTrue(m.nextIsString());
		assertEquals("TDDeviceEvent", m.takeString());

		assertTrue(m.nextIsInt());
		assertEquals(1, m.takeInt());

		assertTrue(m.nextIsInt());
		assertEquals(1, m.takeInt());

		assertTrue(m.nextIsString());
		assertEquals("0", m.takeString());
	}

	@Test
	public void testFromString() {
		// Test some real
		m = Message.fromString("13:TDDeviceEventi1si1s1:0");
		assertTrue(m.nextIsString());
		assertEquals("TDDeviceEvent", m.takeString());

		assertTrue(m.nextIsInt());
		assertEquals(1, m.takeInt());

		assertTrue(m.nextIsInt());
		assertEquals(1, m.takeInt());

		assertTrue(m.nextIsString());
		assertEquals("0", m.takeString());
	}

}
