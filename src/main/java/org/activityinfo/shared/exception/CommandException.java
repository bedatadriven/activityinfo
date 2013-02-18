/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.exception;

import org.activityinfo.shared.command.result.CommandResult;

public class CommandException extends RuntimeException implements CommandResult {


    public CommandException(String message) {
        super(message);
    }

    public CommandException() {
	}

    public CommandException(Throwable e) {
        super(e);
    }
}
