package org.activityinfo.client.local.command.handler;

import java.util.Random;

/**
 * Generates unique ids on the client side.
 * Currently uses randomness.
 * 
 * @author alex
 *
 */
public class KeyGenerator {

	private final Random random = new Random();
	
	private static final int MIN_KEY = 2^14;
	
	
	/**
	 * 
	 * @return a random 32-bit integer key
	 */
	public int generateInt() {
		return random.nextInt(Integer.MAX_VALUE - MIN_KEY) + MIN_KEY;
	}
}
