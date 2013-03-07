package org.activityinfo.server.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.apache.commons.codec.binary.Base64;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import freemarker.template.Configuration;

/**
 * Sends emails through the Postmark API using the HTTP API.
 * 
 * <p>
 * PostMark is a service for transactional email that help ensure a high
 * delivery rate.
 * 
 * <p>
 * The API key can be set in the configuration file with the property
 * {@code postmark.key }
 * 
 * <p>
 * The docs are available from
 * http://developer.postmarkapp.com/developer-build.html
 */
public class PostmarkMailSender extends MailSender {

    public static final String POSTMARK_API_KEY = "postmark.key";

    private final String apiKey;

    private static final Logger LOGGER = Logger
        .getLogger(PostmarkMailSender.class.getName());

    @Inject
    public PostmarkMailSender(DeploymentConfiguration deploymentConfig,
        Configuration templateCfg) {
        super(templateCfg);
        this.apiKey = deploymentConfig.getProperty(POSTMARK_API_KEY);
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
        conn.setRequestProperty("X-Postmark-Server-Token", apiKey);
        conn.setRequestProperty("Content-Type",
            "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5 * 60 * 1000);
        conn.setReadTimeout(5 * 60 * 1000);

        OutputStreamWriter writer = new OutputStreamWriter(
            conn.getOutputStream(), Charsets.UTF_8);
        writer.write(node.toString());
        writer.flush();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        writer.close();
        reader.close();
    }

    private JsonObject toJson(Message message) throws MessagingException,
        IOException {
        JsonObject json = new JsonObject();
        json.addProperty("From", "notifications@activityinfo.org");
        json.addProperty("To", toString(message.getTo()));
        json.addProperty("Subject", message.getSubject());

        if (message.hasTextBody()) {
            json.addProperty("TextBody", message.getTextBody());
        }
        if (message.hasHtmlBody()) {
            json.addProperty("HtmlBody", message.getSafeHtmlBody());
        }

        JsonArray attachments = new JsonArray();
        for (MessageAttachment part : message.getAttachments()) {
            JsonObject attachment = new JsonObject();
            attachment.addProperty("Name", part.getFilename());
            attachment.addProperty("Content", toBase64(part));
            attachment.addProperty("ContentType",
                stripContentType(part.getContentType()));
            attachments.add(attachment);
        }
        if (attachments.size() > 0) {
            json.add("Attachments", attachments);
        }

        if (message.getReplyTo() != null) {
            json.addProperty("ReplyTo",
                toString(Arrays.asList(message.getReplyTo())));
        }
        return json;
    }

    private String stripContentType(String contentType) {
        int semicolon = contentType.indexOf(';');
        if (semicolon == -1) {
            return contentType.trim();
        }
        return contentType.substring(0, semicolon).trim();
    }

    private String toBase64(MessageAttachment part) throws IOException {
        return new String(Base64.encodeBase64(part.getContent()));
    }

    private String toString(Iterable<InternetAddress> addresses) {
        StringBuilder sb = new StringBuilder();
        if (addresses != null) {
            for (InternetAddress address : addresses) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(address.getAddress());
            }
        }
        return sb.toString();
    }
}
