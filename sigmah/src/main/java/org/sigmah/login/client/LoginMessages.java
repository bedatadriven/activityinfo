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

package org.sigmah.login.client;

import com.google.gwt.i18n.client.Messages;

public interface LoginMessages extends Messages {

    @Messages.DefaultMessage("Please enter your email address to log on.")
    String emailAddressMissing();

    @Messages.DefaultMessage("Please enter a valid email address to log on.")
    String emailAddressInvalid();

    @Messages.DefaultMessage("Please enter your password to continue")
    String passwordMissing();

    @Messages.DefaultMessage("The ActivityInfo server is temporarily unavailable, please try again in a moment.")
    String statusCodeException();

    @Messages.DefaultMessage("There was a problem connecting to the server, please check your internet connection and try again.")
    String invocationException();

    @Messages.DefaultMessage("Connecting...")
    String connecting();

    @Messages.DefaultMessage("Login successful, loading app...")
    String loginSuccessful();

    @DefaultMessage("Incorrect email or password. Double check and try again.")
    String invalidLogin();
}
