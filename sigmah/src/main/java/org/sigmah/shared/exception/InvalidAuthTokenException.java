/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.exception;

public class InvalidAuthTokenException extends CommandException {

	public InvalidAuthTokenException() {
		
	}

	public InvalidAuthTokenException(String message) {
		super(message);
	}


}
