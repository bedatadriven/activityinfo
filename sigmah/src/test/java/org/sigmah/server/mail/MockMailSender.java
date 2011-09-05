/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import com.google.common.collect.Lists;

/**
 * @author Alex Bertram
 */
public class MockMailSender implements MailSender {

    public List<MailMessage> sentMail = Lists.newArrayList();
    public List<Email> sentEmail = Lists.newArrayList();


    @Override
    public void send(MailMessage message) {
        sentMail.add(message);
    }


	@Override
	public void send(Email email) throws EmailException {
		sentEmail.add(email);
		
	}
}
