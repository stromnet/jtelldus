package se.stromnet.jtelldus;

import se.stromnet.jtelldus.event.TelldusEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.stromnet.jtelldus.Protocol.ErrorCode;
import se.stromnet.jtelldus.event.EventDispatcher;
import se.stromnet.jtelldus.event.EventFactory;

/**
 * Implements the client communications. Opens up one event socket, and provides
 * the basics for TelldusInterface to execute commands.
 *
 * Similar to client/Client.cpp
 *
 * @author johan
 */
public class TelldusClient implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(TelldusClient.class);

	private TelldusSocket client, events;
	protected Thread thread;

	private EventDispatcher dispatcher;
	private boolean run = true;
	

	public TelldusClient() {
		dispatcher = new EventDispatcher();
	}
	
	public TelldusClient(String host, int clientPort, int eventPort) throws IOException {
		this();
		start(host, clientPort, eventPort);
	}
	
	public void start(String host, int clientPort, int eventPort) throws IOException
	{
		client = new TelldusSocket(host, clientPort);
		events = new TelldusSocket(host, eventPort);

		thread = new Thread(this);
		thread.start();
	}

	public void close() {
		run = false;
		if(thread == null)
			return;
		
		try {
			thread.interrupt();
			thread.join();
		}catch(Exception ex) {}

		thread = null;
	}

	/**
	 * Main loop for separate Thread. keeps connection to event socket
	 * alive and reads events.
	 */
	@Override
	public void run() {
		while (run) {
			if (!events.connect()) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				continue;
			}

			try {
				// Use same buffer size as ClientCommunicationHandler.cpp
				ByteBuffer bb = ByteBuffer.allocate(2000);
				while (run) {
					log.trace("Reading...");
					int len = events.read(bb);

					if (!Message.nextIsString(bb)) {
						throw new IllegalStateException("Illegal data in buffer, expected string");
					}

					Message msg = new Message(bb);
					if(!handleEvent(msg)) {
						// Unhandled; clear out bb
						bb.clear();
					}
				}
			} catch(ClosedByInterruptException ex) {
				// Most likely close() was called; run will be false.
			} catch (IOException ex) {
				log.error("Error talking to telldusd", ex);
			} catch (IllegalStateException ex) {
				log.error("Unexpected state", ex);
				// Disconnect and hope a reconnect solves it.
				try {
					events.disconnect();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Extract event and dispatch if proper
	 */
	private boolean handleEvent(Message msg) {
		TelldusEvent event = EventFactory.createEvent(msg);

		if(event == null)
			return false;

		dispatcher.dispatchEvent(event);

		return true;
	}

	public void registerEventListener(TelldusEvent.Listener listener) {
		dispatcher.addEventListener(listener);
	}



	/** Controlling help functions **/
	protected boolean getBoolFromService(Message m) {
		return getIntegerFromService(m) == ErrorCode.SUCCESS.code();
	}

	protected int getIntegerFromService(Message m) {
		ByteBuffer bb = sendToService(m);

		return Message.takeInt(bb);
	}

	protected String getStringFromService(Message m) {
		ByteBuffer bb = sendToService(m);

		return Message.takeString(bb);
	}

	protected ByteBuffer sendToService(Message m) {
		return sendToService(m, true);
	}

	private ByteBuffer sendToService(Message m, boolean retry) {
		/**
		 * Unfortunately telldusd cannot handle more than one message
		 * per connection..
		 */
		try {
			// Socket is generally already disconnected.. But for retrys, might not be.

			// XXX: trunk client has retry support.
			client.disconnect();

			client.connect();
			client.write(m);

			ByteBuffer bb = ByteBuffer.allocate(2000);
			client.read(bb);

			return bb;
		} catch (IOException ex) {

			if (retry) // Retry once
			{
				return sendToService(m, false);
			}

			return null;
		} finally {
			// Always disconnect.
			try {
				client.disconnect();
			} catch (IOException ex) {
			}
		}

	}


}
