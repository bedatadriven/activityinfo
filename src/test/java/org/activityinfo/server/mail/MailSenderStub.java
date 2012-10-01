/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.mail;

import java.util.List;

import javax.inject.Inject;
import javax.mail.Message;

import org.activityinfo.test.TestScoped;

import com.google.common.collect.Lists;

import freemarker.template.Configuration;

@TestScoped
public class MailSenderStub extends MailSender {

	@Inject
    public MailSenderStub(Configuration templateCfg) {
		super(templateCfg);
	}

	public List<Message> sentMail = Lists.newArrayList();


    @Override
    public void send(Message message) {
        sentMail.add(message);
    }
}
