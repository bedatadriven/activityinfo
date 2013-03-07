package org.activityinfo.server.mail;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.common.collect.Lists;



public class Message {

    
    private List<InternetAddress> to = Lists.newArrayList();
    private List<InternetAddress> bcc = Lists.newArrayList();
    private InternetAddress replyTo = null;
    private List<MessageAttachment> attachments = Lists.newArrayList();
    private String subject;
    private String textBody;
    private String htmlBody;
   

    public Message to(String email, String name)
        throws MessagingException {
        try {
            to.add(new InternetAddress(
                email, name));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Message replyTo(String email, String name)
        throws MessagingException {
        InternetAddress address;
        try {
            address = new InternetAddress(email, name);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e); // should not happen
        }
        replyTo = address;
        return this;
    }

    public Message to(String email) throws AddressException  {
        to.add(new InternetAddress(email));
        return this;
    }

    public void bcc(String email) throws AddressException {
        bcc.add(new InternetAddress(email));
    }

    public Message subject(String subject)  {
        this.subject = subject;
        return this;
    }

    public void body(String text)  {
        textBody = text;
    }

    public void htmlBody(String html)  {
        htmlBody = html;
    }

    public MessageAttachment addAttachment()  {
        MessageAttachment attachment = new MessageAttachment();
        attachments.add(attachment);
        return attachment;
    }
    
    

    public List<InternetAddress> getTo() {
        return to;
    }

    public List<InternetAddress> getBcc() {
        return bcc;
    }

    public InternetAddress getReplyTo() {
        return replyTo;
    }

    public List<MessageAttachment> getAttachments() {
        return attachments;
    }

    public String getSubject() {
        return subject;
    }

    public String getTextBody() {
        return textBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }
    
    public String getSafeHtmlBody() {
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i != htmlBody.length(); ++i) {
            int cp = htmlBody.codePointAt(i);
            if (cp <= 127) {
                encoded.appendCodePoint(cp);
            } else {
                encoded.append("&#").append(cp).append(';');
            }
        }
        return encoded.toString();
    }

    public boolean hasTextBody() {
        return textBody != null;
    }
    
    public boolean hasHtmlBody() {
        return htmlBody != null;
    }


}
