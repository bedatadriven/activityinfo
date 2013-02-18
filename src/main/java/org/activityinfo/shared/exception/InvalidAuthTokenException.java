/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.exception;

/**
 * Indicates that the auth token is missing from the request, is not found in the 
 * database, or has expired.
 */
public class InvalidAuthTokenException extends CommandException {

	public InvalidAuthTokenException() {
		
	}

	public InvalidAuthTokenException(String message) {
		super(message);
	}


}
