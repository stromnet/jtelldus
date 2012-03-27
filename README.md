JTelldus
========

JTelldus is a pure java client for talking to a telldusd.

Telldus is a combination of software/hardware from Telldus Technologies (http://www.telldus.com/).
The hardware allows for sending RF signals, allowing computer control of devices in your home.
Telldusd is the daemon in control of the hardware.

Current state
-------------
The initial version of this library (March 2012) is written according to the specifications (or rather, the code) of the telldus trunk (https://github.com/telldus/telldus/).
Both sending controlling and events are supported.

However, it has not undergone very much real-life testing (basically only test/../TelldusClientTest.java)

Dependencies
------------
The only extenral dependency is slf4j (http://www.slf4j.org/) for logging. Also, `socat` or similar is required (see quirk below).

Building
--------
Currently there are no build files commited, since they are tied in to some other generic build files I've got. But a basic ant file should do.

Quirk
-----
Since telldusd listens to two unix sockets (/tmp/TelldusClient and /tmp/TelldusEvent), we got some problems talking to it from pure java, UNIX sockets are not supported
in normal Java.

Workaround
----------
Until (if ever) telldusd can listen to TCP ports direclty, use `socat` (http://www.dest-unreach.org/socat/) or similar utility to bridge these UNIX sockets to regular TCP sockets.
Example commands:

	socat tcp-listen:9999,fork unix-connect:/tmp/TelldusEvents &
	socat tcp-listen:9998,fork unix-connect:/tmp/TelldusClient &

This is clearly not optimal, but better than requiering some kind of native-lib.

Refs
----



