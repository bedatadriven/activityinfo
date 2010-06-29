/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
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
