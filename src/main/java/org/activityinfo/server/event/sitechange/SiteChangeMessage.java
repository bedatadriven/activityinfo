package org.activityinfo.server.event.sitechange;

import java.util.Date;

import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailMessage;

public class SiteChangeMessage extends MailMessage {
	private User recipient;
    private User editor;
    private Site site;
    private Date date;

    public SiteChangeMessage(User recipient, User editor, Site site, Date date) {
        this.recipient = recipient;
        this.editor = editor;
        this.site = site;
        this.date = date;
    }
    
	@Override
	public User getRecipient() {
		return recipient;
	}

    public User getEditor() {
    	return editor;
    }

    public Date getDate() {
    	return date;
    }

    public Site getSite() {
    	return site;
    }
}
