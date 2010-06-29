/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

public interface MailSender {

    public void send(Email message) throws EmailException;
}
