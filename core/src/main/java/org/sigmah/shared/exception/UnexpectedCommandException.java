/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.exception;

/*
 * @author Alex Bertram
 */
public class UnexpectedCommandException extends CommandException {


    public UnexpectedCommandException() {
    }

    public UnexpectedCommandException(String message) {
        super(message);
    }

    public UnexpectedCommandException(Throwable e) {
        super(e);
    }
}
