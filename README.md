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
The only external dependency is slf4j (http://www.slf4j.org/) for logging. Also, a patched telldusd or `socat` (or similar) is required (see quirk below).

Building
--------
Currently there are no build files commited, since they are tied in to some other generic build files I've got. But a basic ant file should do.

Quirk
-----
The trunk version of telldusd listens to two unix sockets (/tmp/TelldusClient and /tmp/TelldusEvent), which we got some problems talking to from pure java; UNIX sockets are not supported in normal Java.

Workaround
----------
I've implemented TCP support in a fork of telldusd. It tries to follow telldusd-trunk, but is not yet ready for merging do trunk.
It can be fetched from https://github.com/stromnet/telldus/tree/tcp.

For background on whats left before a merge can happen, please see http://www.telldus.com/forum/viewtopic.php?f=22&t=1966 (Swedish only though).

For those of you not wanting to run my patched telldusd, use `socat` (http://www.dest-unreach.org/socat/) or similar utility to bridge these UNIX sockets to regular TCP sockets.
Example commands:

	socat tcp-listen:9999,fork unix-connect:/tmp/TelldusEvents &
	socat tcp-listen:9998,fork unix-connect:/tmp/TelldusClient &

This is clearly not optimal, but better than requiering some kind of native-lib.

Who & why?
----------
JTelldus is written by Johan Ström, head of Stromnet.
The goal is to be able to implement telldus support into OpenHAB (http://code.google.com/p/openhab/), but doing so in a clean mannor (ie, not some native library).

License
-------
LGPL


