/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.util.logging.LogException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;



public abstract class MailSender {

	private final Configuration templateCfg;
	
	public MailSender(Configuration templateCfg) {
		super();
		this.templateCfg = templateCfg;
	}

	public abstract void send(Message message) throws MessagingException;

	@LogException
	public void send(MailMessage model) {
		try {
			MessageBuilder message = createMessage(model);
			send(message.build());
			
		} catch(MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	public MessageBuilder createMessage(MailMessage model)
			throws MessagingException, AddressException, IOException,
			TemplateException {
		MessageBuilder message = new MessageBuilder();
		message.to(model.getRecipient().getEmail(), model.getRecipient().getName());
		message.bcc("akbertram@gmail.com");
		message.subject(getSubject(model));
		message.body(composeMessage(model));
		return message;
	}

	private String composeMessage(MailMessage model)
			throws IOException, TemplateException {

		StringWriter writer = new StringWriter();
		Template template = templateCfg.getTemplate(model.getTemplateName(), 
				LocaleHelper.getLocaleObject(model.getRecipient()));
		template.process(model, writer);
		return writer.toString();
	}

	private String getSubject(MailMessage message) {
		String subject = getResourceBundle(message).getString(message.getSubjectKey());
		if(subject == null) {
			throw new RuntimeException("Missing subject key '" + message.getSubjectKey() + "' in MailMessages");
		}
		return subject;
	}

	private ResourceBundle getResourceBundle(MailMessage message) {
		return ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", 
				LocaleHelper.getLocaleObject(message.getRecipient()));
	}
}

