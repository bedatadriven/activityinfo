package org.activityinfo.server.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
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
import javax.mail.util.ByteArrayDataSource;


public class MessageBuilder {

	public Multipart mp = null;
	private MimeMessage msg;

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
		msg.setSubject(subject, "utf-8");
		return this;
	}

	public void body(String text) throws MessagingException {
		msg.setText(text, "utf-8");
	}
	
	public void htmlBody(String html)throws MessagingException {
		msg.setDataHandler(new DataHandler(new HTMLDataSource(html)));
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
		msg.saveChanges();
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
			DataSource src = new ByteArrayDataSource(content, mimeType);
			attachment.setDataHandler(new DataHandler(src));
			return this;
		}

		public PartBuilder withText(String text) throws MessagingException {
			attachment.setText(text);
			return this;
		}
	}

	static class HTMLDataSource implements DataSource {
        private String html;
 
        public HTMLDataSource(String htmlString) {
        	// transform non-ascii characters into entities to avoid 
        	// encoding issues
        	StringBuilder encoded = new StringBuilder();
        	for(int i=0;i!=htmlString.length();++i) {
        		int cp = htmlString.codePointAt(i);
        		if(cp <= 127) {
        			encoded.appendCodePoint(cp);
        		} else {
        			encoded.append("&#").append(cp).append(';');
        		}
        	}
        	
            html = encoded.toString();
        }
 
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("Null HTML");
            return new ByteArrayInputStream(html.getBytes());
        }
 
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }
 
        public String getContentType() {
            return "text/html; charset=UTF-8";
        }
 
        public String getName() {
            return "text/html dataSource";
        }
    }

}
