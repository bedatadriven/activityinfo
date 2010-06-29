/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
