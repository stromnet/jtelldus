package se.stromnet.jtelldus;

import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Builds a Message used for communicating with Telldusd.
 *
 * Handles serialization and de-serialization.
 *
 * Based of http://developer.telldus.se/browser/telldus-core/common/Message.cpp.
 *
 * telldusd works with std::wstring's, in the C++ impl the whole class
 * extends std::wstring. Here we use a StringBuilder, and on serialization
 * we make sure to encode it in UTF-8 as per common/Strings.cpp wideToString
 * which is used when talking on the socket (Socket::write).
 * 
 * @author johan
 */
public class Message {
	private StringBuilder sb;
	private ByteBuffer bytes;
	
	public Message() {
		reset();
	}

	public Message(ByteBuffer bb) {
		this();
		this.bytes = bb;
	}

	public Message(String functionName) {
		this();
		addArgument(functionName);
	}

	public void reset() {
		sb = new StringBuilder();
		bytes = null;
	}

	public void addArgument(String argument) {
		sb.append(argument.length())
		  .append(":")
		  .append(argument);
	}

	public void addArgument(int argument) {
		sb.append("i")
		  .append(argument)
		  .append("s");
	}

	public ByteBuffer serialize() {
		String str = sb.toString();

		try {
			byte[] bytes = str.getBytes("UTF-8");
			return ByteBuffer.wrap(bytes);
		}catch(UnsupportedEncodingException ex) {
			// Should never happen.. Re-throw as RTE instead of requiring calling
			// code to handle UEE.
			throw new RuntimeException("UTF-8 not supported", ex);
		}
	}

	public static Message fromString(String str) {
		try {
			byte[] bytes = str.getBytes("UTF-8");
			return new Message(ByteBuffer.wrap(bytes));
		}catch(UnsupportedEncodingException ex) {
			// Should never happen.. Re-throw as RTE instead of requiring calling
			// code to handle UEE.
			throw new RuntimeException("UTF-8 not supported", ex);
		}
	}

	public ByteBuffer getBuffer() {
		return bytes;
	}
	
	public boolean nextIsInt() {
		return nextIsInt(bytes);
	}

	public boolean nextIsString() {
		return nextIsString(bytes);
	}

	public int takeInt() {
		return takeInt(bytes);
	}

	public String takeString() {
		return takeString(bytes);
	}

	public static boolean nextIsInt(ByteBuffer msg) {
		if(msg.position() != 0)
			msg.flip();

		if(msg == null || !msg.hasRemaining())
			return false;

		msg.mark();
		byte b = msg.get();
		msg.reset();
		
		return b == 'i';
	}

	public static boolean nextIsString(ByteBuffer msg) {
		if(msg.position() != 0)
			msg.flip();

		if(msg == null || !msg.hasRemaining())
			return false;

		msg.mark();
		byte b = msg.get();
		msg.reset();
		
		return Character.isDigit(b);
	}

	public static int takeInt(ByteBuffer msg) {
		if(!nextIsInt(msg))
			throw new BufferUnderflowException();

		msg.position(1); // Skip 'i'

		// Read until 's'. There are probably more elegant solutinos to this
		// but since we're dealing with stringified numbers, and my NIO Buffer
		// skills are limited....well :)
		StringBuilder sb = new StringBuilder();
		while(msg.hasRemaining()) {
			byte b = msg.get();

			if(b == 's')
				break;

			assert Character.isDigit(b);
			sb.append(Character.toString((char) b));
		}

		int ret = Integer.parseInt(sb.toString());

		msg.compact();

		return ret;
	}

	public static String takeString(ByteBuffer msg) {
		if(!nextIsString(msg))
			throw new BufferUnderflowException();

		msg.mark();
		
		// First read stringified length.
		StringBuilder sb = new StringBuilder();
		while(msg.hasRemaining()) {
			byte b = msg.get();
			if(b == ':')
				break;

			assert Character.isDigit(b);
			sb.append(Character.toString((char) b));
		}

		int len = Integer.parseInt(sb.toString());

		if(msg.remaining() < len) {
			// Buffer incomplete; go back to where we where.
			msg.position(msg.limit());
			msg.limit(msg.capacity());
			
			throw new BufferUnderflowException();
		}

		// Read this many bytes
		byte[] buf = new byte[len];
		msg.get(buf);

		msg.compact();

		try {
			return new String(buf, "UTF-8");
		}catch(UnsupportedEncodingException ex) {
			throw new RuntimeException("UTF-8 not supported", ex);
		}
	}
}
