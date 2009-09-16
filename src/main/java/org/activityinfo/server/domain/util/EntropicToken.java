package org.activityinfo.server.domain.util;

import java.security.SecureRandom;

public class EntropicToken {

	public static int TOKEN_SIZE = 16; // 128 bits
	
	private static SecureRandom SECURE_RANDOM = new SecureRandom();	
	
	public static String generate() {
		byte[] token = new byte[TOKEN_SIZE];
		SECURE_RANDOM.nextBytes(token);		
	
		return stringFromBytes(token);
	}
	
	public static String stringFromBytes(byte[] bytes) {
	
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i!=bytes.length; i++) {
			sb.append(byteToHex(bytes[i]));
		}
		return sb.toString();
	}
	
	private static String byteToHex(byte b) {
		StringBuilder sb = new StringBuilder();
		sb.append( nibble2char( (byte) ( (b & 0xf0) >> 4)) );
	    sb.append( nibble2char( (byte)   (b & 0x0f) ) );
	    return sb.toString();
	}
	
	private static char nibble2char(byte b) {
	    byte nibble = (byte) (b & 0x0f);
	    if (nibble < 10) {
	        return (char) ('0' + nibble);
	    }
	    return (char) ('a' + nibble - 10);
	}
}