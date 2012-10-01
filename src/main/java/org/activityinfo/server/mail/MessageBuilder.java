package org.activityinfo.server.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class MessageBuilder {

	public Multipart mp = null;
	private Message msg;

	public MessageBuilder() throws MessagingException {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
		msg = new MimeMessage(session);
	}
	
	public MessageBuilder to(String email, String name) throws MessagingException {
		try {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, name));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}


	public MessageBuilder to(String email) throws MessagingException {
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		return this;
	}


	public void bcc(String email) throws AddressException, MessagingException {
		msg.addRecipient(RecipientType.BCC, new InternetAddress(email));
	}

	public MessageBuilder subject(String subject) throws MessagingException {
		msg.setSubject(subject);
		return this;
	}

	public void body(String text) throws MessagingException {
		msg.setText(text);
	}

	public PartBuilder addPart() throws MessagingException {
		if(mp == null) {
			mp = new MimeMultipart();
		}
		return new PartBuilder();
	}
	
	
	public Message build() throws MessagingException {
		if(mp != null) {
			msg.setContent(mp);
		}
		return msg;
	}
	
	public class PartBuilder {
		
		private MimeBodyPart attachment;

		private PartBuilder() throws MessagingException {
			attachment = new MimeBodyPart();
			mp.addBodyPart(attachment);
		}
		
		public PartBuilder withFileName(String filename) throws MessagingException {
			attachment.setFileName(filename);
			return this;
		}
		public PartBuilder withContent(byte[] content, String mimeType) throws MessagingException {
			attachment.setContent(content, mimeType);
			return this;
		}

		public PartBuilder withText(String text) throws MessagingException {
			attachment.setText(text);
			return this;
		}
	}

	

}
