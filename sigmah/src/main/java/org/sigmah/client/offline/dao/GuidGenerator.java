/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

/*
 * Generate a random uuid.
 *
 * USAGE: Math.uuid(length, radix)
 *   length - the desired number of characters
 *   radix  - the number of allowable values for each character.
 *
 * EXAMPLES:
 *   // No arguments  - returns RFC4122, version 4 ID
 *   >>> Math.uuid()
 *   "92329D39-6F5C-4520-ABFC-AAB64544E172"
 *
 *   // One argument - returns ID of the specified length
 *   >>> Math.uuid(15)     // 15 character ID (default base=62)
 *   "VcydxgltxrVZSTV"
 *
 *   // Two arguments - returns ID of the specified length, and radix. (Radix must be <= 62)
 *   >>> Math.uuid(8, 2)  // 8 character ID (base=2)
 *   "01001010"
 *   >>> Math.uuid(8, 10) // 8 character ID (base=10)
 *   "47473046"
 *   >>> Math.uuid(8, 16) // 8 character ID (base=16)
 *   "098F4D35"
 */
package org.sigmah.client.offline.dao;

import com.google.gwt.user.client.Random;

/**
 *
 * Generates a GUID
 *
 * @author Robert Kieffer
 * @author Alex Bertram
 */
public class GuidGenerator {

    private static final char[] CHARS  = getChars();

    /**
     * Generates a GUID in compact form.
     *
     * Examples: length = "VcydxgltxrVZSTV"
     *
     * @param length The number of digits (characters)
     * @param radix Radix (maximum 62)
     * @return GUID in compact form
     */
    public static String generateCompact(int length, int radix) {

        char[] uuid = new char[36];

        for(int i=0; i!=length;++i) {
            uuid[i] = CHARS[ Random.nextInt(radix) ];
        }
        return new String(uuid);
    }

    public static String generateV4() {

        char[] uuid = new char[36];

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (int i = 0; i != 36; i++) {
          if (uuid[i]==0) {
            int r = Random.nextInt(16);
            uuid[i] = CHARS[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
          }
        }
        return new String(uuid);
    }

    // probably the most compact way of expressing this !
    private static native char[] getChars() /*-{
        return '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    }-*/;



}
