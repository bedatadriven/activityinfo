/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.exception;

/**
 * Exception indicating that a partner of this name 
 * already exists within the database
 */
public class DuplicatePartnerException extends CommandException {

    public DuplicatePartnerException() {
    }
}
