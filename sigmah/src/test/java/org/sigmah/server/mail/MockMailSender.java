/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class MockMailSender implements MailSender {

    public List<Email> sentMail = new ArrayList<Email>();


    @Override
    public void send(Email message) throws EmailException {
        sentMail.add(message);
    }
}
