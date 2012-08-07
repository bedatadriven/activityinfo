package org.activityinfo.server.util.monitoring;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Locale;
import java.util.Random;

import org.apache.log4j.Logger;

public class StatsdClient {
	
	private static Logger LOGGER = Logger.getLogger(StatsdClient.class);
	
	private final InetSocketAddress address;
	private final DatagramChannel channel;
	
	private final Random RNG = new Random();

	public StatsdClient(InetSocketAddress address) throws IOException {
		super();
		this.address = address;
		this.channel = DatagramChannel.open();
	}
	
	public boolean timing(String key, int value) {
		return timing(key, value, 1.0);
	}

	public boolean timing(String key, int value, double sampleRate) {
		return send(sampleRate, String.format(Locale.ENGLISH, "%s:%d|ms", key, value));
	}

	public boolean decrement(String key) {
		return increment(key, -1, 1.0);
	}

	public boolean decrement(String key, int magnitude) {
		return decrement(key, magnitude, 1.0);
	}

	public boolean decrement(String key, int magnitude, double sampleRate) {
		magnitude = magnitude < 0 ? magnitude : -magnitude;
		return increment(key, magnitude, sampleRate);
	}

	public boolean decrement(String... keys) {
		return increment(-1, 1.0, keys);
	}

	public boolean decrement(int magnitude, String... keys) {
		magnitude = magnitude < 0 ? magnitude : -magnitude;
		return increment(magnitude, 1.0, keys);
	}

	public boolean decrement(int magnitude, double sampleRate, String... keys) {
		magnitude = magnitude < 0 ? magnitude : -magnitude;
		return increment(magnitude, sampleRate, keys);
	}

	public boolean increment(String key) {
		return increment(key, 1, 1.0);
	}

	public boolean increment(String key, int magnitude) {
		return increment(key, magnitude, 1.0);
	}

	public boolean increment(String key, int magnitude, double sampleRate) {
		String stat = String.format(Locale.ENGLISH, "%s:%s|c", key, magnitude);
		return send(sampleRate, stat);
	}

	public boolean increment(int magnitude, double sampleRate, String... keys) {
		String[] stats = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			stats[i] = String.format(Locale.ENGLISH, "%s:%s|c", keys[i], magnitude);
		}
		return send(sampleRate, stats);
	}

	public boolean gauge(String key, int magnitude){
		return gauge(key, magnitude, 1.0);
	}

	public boolean gauge(String key, int magnitude, double sampleRate){
		final String stat = String.format(Locale.ENGLISH, "%s:%s|g", key, magnitude);
		return send(sampleRate, stat);
	}

	private boolean send(double sampleRate, String... stats) {

		boolean retval = false; // didn't send anything
		if (sampleRate < 1.0) {
			for (String stat : stats) {
				if (RNG.nextDouble() <= sampleRate) {
					stat = String.format(Locale.ENGLISH, "%s|@%f", stat, sampleRate);
					if (doSend(stat)) {
						retval = true;
					}
				}
			}
		} else {
			for (String stat : stats) {
				if (doSend(stat)) {
					retval = true;
				}
			}
		}

		return retval;
	}

	private boolean doSend(final String stat) {
		try {
			final byte[] data = stat.getBytes("utf-8");
			final ByteBuffer buff = ByteBuffer.wrap(data);
			final int nbSentBytes = channel.send(buff, address);

			if (data.length == nbSentBytes) {
				return true;
			} else {
				LOGGER.error(String.format(
						"Could not send entirely stat %s to host %s:%d. Only sent %d bytes out of %d bytes", stat,
						address.getHostName(), address.getPort(), nbSentBytes, data.length));
				return false;
			}

		} catch (IOException e) {
			LOGGER.error(
					String.format("Could not send stat %s to host %s:%d", stat, address.getHostName(),
							address.getPort()), e);
			return false;
		}
	}
}
