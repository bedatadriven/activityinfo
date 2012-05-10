/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.mail;

import java.util.List;

import org.activityinfo.server.mail.MailMessage;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.test.TestScoped;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import com.google.common.collect.Lists;

@TestScoped
public class MailSenderStub implements MailSender {

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
