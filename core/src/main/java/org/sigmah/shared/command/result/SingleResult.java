/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

public interface SingleResult<T> extends CommandResult {

	public T getResult();

}
