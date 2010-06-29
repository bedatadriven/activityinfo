/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.auth;

import org.junit.Ignore;
import org.junit.Test;
import org.sigmah.server.auth.impl.BCrypt;

import static org.junit.Assert.assertTrue;

public class BCryptTest {

    @Test
    public void realityCheck() {

        String input = "Hello world";

        String hashed = BCrypt.hashpw(input, BCrypt.gensalt());

        assertTrue(BCrypt.checkpw(input, hashed));
    }

    @Test
    @Ignore("for generating sample dbunit files only")
    public void generateSamples() {
        printSample("monday");
        printSample("tuesday");
    }

    private void printSample(String password) {
        System.out.println(password + " = " + BCrypt.hashpw(password, BCrypt.gensalt()));
    }
}
