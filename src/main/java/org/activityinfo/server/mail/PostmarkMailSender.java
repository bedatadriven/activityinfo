package org.activityinfo.server.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.apache.commons.codec.binary.Base64;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import freemarker.template.Configuration;

/**
 * Sends emails through the Postmark API using the HTTP API.
 *
 * <p>PostMark is a service for transactional email that help ensure
 * a high delivery rate. 
 * 
 * <p>The API key can be set in the configuration file 
 * with the property {@code postmark.key }
 * 
 * <p>The docs are available from http://developer.postmarkapp.com/developer-build.html
 */
public class PostmarkMailSender extends MailSender {

	private final String apiKey;
	
	private static final Logger LOGGER = Logger.getLogger(PostmarkMailSender.class.getName());

	@Inject
	public PostmarkMailSender(DeploymentConfiguration deploymentConfig, Configuration templateCfg) {
		super(templateCfg);
		this.apiKey = deploymentConfig.getProperty("postmark.key");
	}

	@Override
	public void send(Message message) throws MessagingException {
		try {
			JsonObject json = toJson(message);
			postMessage(json);
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	
	private void postMessage(JsonObject node) throws IOException {
		URL url = new URL("http://api.postmarkapp.com/email");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty ("X-Postmark-Server-Token", apiKey);
		conn.setRequestProperty ("Content-Type", "application/json");
		conn.setRequestProperty ("Accept", "application/json");
		
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	    writer.write(node.toString());
	    writer.flush();
	    String line;
	    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	    }
	    writer.close();
	    reader.close();	
	}

	private JsonObject toJson(Message message) throws MessagingException, IOException {
		JsonObject json = new JsonObject();
		json.addProperty("From", "notifications@activityinfo.org");
		json.addProperty("To", toString(message.getRecipients(RecipientType.TO)));
		json.addProperty("Subject", message.getSubject());
		if(message.getContentType().startsWith("text/plain")) {
			json.addProperty("TextBody", message.getContent().toString());
		} else if(message.getContent() instanceof MimeMultipart ) {
			MimeMultipart multipart = (MimeMultipart) message.getContent();
			JsonArray attachments = new JsonArray();
			for(int i=0;i!=multipart.getCount();++i) {
				BodyPart part = multipart.getBodyPart(i);
				if(part.getContentType().startsWith("text/plain")) {
					json.addProperty("TextBody", part.toString());
				} else {
					JsonObject attachment = new JsonObject();
					attachment.addProperty("Name", part.getFileName());
					attachment.addProperty("Content", toBase64(part));
					attachment.addProperty("ContentType", stripContentType(part.getContentType()));
					attachments.add(attachment);
				}
			}
			json.add("Attachments", attachments);
		}
		if(message.getReplyTo() != null && message.getReplyTo().length > 0) {
			json.addProperty("ReplyTo", toString(message.getReplyTo()));
		}
		return json;
	}

	private String stripContentType(String contentType) {
		int semicolon = contentType.indexOf(';');
		if(semicolon == -1) {
			return contentType.trim();
		}
		return contentType.substring(0, semicolon).trim();
	}

	private String toBase64(BodyPart part) throws IOException,
			MessagingException {
		InputStream in = (InputStream) part.getContent();
		byte[] bytes = ByteStreams.toByteArray(in);
		String encoded = new String(Base64.encodeBase64(bytes));
		return encoded;
	}

	private String toString(Address[] addresses) {
		StringBuilder sb = new StringBuilder();
			if(addresses != null) {
			for(Address address : addresses) {
				if(sb.length() > 0) {
					sb.append(", ");
				}
				sb.append( ((InternetAddress)address).getAddress() );
			}
		}
		return sb.toString();
	}
}
