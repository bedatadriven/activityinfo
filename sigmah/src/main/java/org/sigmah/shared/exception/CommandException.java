/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.exception;

import org.sigmah.shared.command.result.CommandResult;

public class CommandException extends Exception implements CommandResult {

	public CommandException(Exception ex) {
		super(ex);
	}

    public CommandException(String message) {
        super(message);
    }

    public CommandException() {
	}

}
