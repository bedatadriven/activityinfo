/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CommandResult;

/**
 *
 * Marker interface for Commands that have
 * an effect on the remote state. (for example,
 * a command which updates the database)
 */
public interface MutatingCommand<R extends CommandResult> extends Command<R> {
	
}
