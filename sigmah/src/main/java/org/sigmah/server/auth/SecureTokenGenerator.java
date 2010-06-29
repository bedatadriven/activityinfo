/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.auth;

import java.security.SecureRandom;

/**
 * Generates a 128-bit random token that can be safely used as a
 * security token.
 */
public class SecureTokenGenerator {

    private static final int TOKEN_SIZE = 16; // 128 bits

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a 128-bit random token and returns it formatted as a
     * 32-digit hexadecimal string
     *
     * @return a 32-digit hexadecimal string
     */
    public static String generate() {
        byte[] token = new byte[TOKEN_SIZE];
        SECURE_RANDOM.nextBytes(token);

        return stringFromBytes(token);
    }

    private static String stringFromBytes(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i != bytes.length; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();
    }

    private static String byteToHex(byte b) {
        StringBuilder sb = new StringBuilder();
        sb.append(nibble2char((byte) ((b & 0xf0) >> 4)));
        sb.append(nibble2char((byte) (b & 0x0f)));
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