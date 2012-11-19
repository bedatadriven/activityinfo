package org.activityinfo.server.event.sitechange;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.form.SiteRenderer;
import org.activityinfo.client.page.entry.form.SiteRenderer.IndicatorValueFormatter;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.server.mail.MessageBuilder;
import org.activityinfo.server.util.html.HtmlWriter;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.MapReportElement;

import com.teklabs.gwt.i18n.server.LocaleProxy;

public class UpdateMessageBuilder {
	
	private static Logger LOGGER = Logger.getLogger(UpdateMessageBuilder.class.getName());
	
	private boolean newSite;
	private User recipient;
	private User editor;
	private Date date;
	private SiteDTO siteDTO;
	private ActivityDTO activityDTO;
	private UserDatabaseDTO userDatabaseDTO;
	
	
	public void setNewSite(boolean newSite) {
		this.newSite = newSite;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public void setEditor(User editor) {
		this.editor = editor;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSiteDTO(SiteDTO siteDTO) {
		this.siteDTO = siteDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}

	public void setUserDatabaseDTO(UserDatabaseDTO userDatabaseDTO) {
		this.userDatabaseDTO = userDatabaseDTO;
	}

	public Message build() throws MessagingException {
		// set the locale of the messages
		LocaleProxy.setLocale(LocaleHelper.getLocaleObject(recipient));
		
		// create message, set recipient & bcc
		MessageBuilder message = new MessageBuilder();
		message.to(recipient.getEmail(), recipient.getName());
		message.bcc("alex@bedatadriven.com");
	    
	    // set the subject 
		String subject;
		if(newSite) {
			subject = I18N.MESSAGES.newSiteSubject(userDatabaseDTO.getName(), activityDTO.getName(), siteDTO.getLocationName(), siteDTO.getPartnerName());
		} else {
			subject = I18N.MESSAGES.updatedSiteSubject(userDatabaseDTO.getName(), activityDTO.getName(), siteDTO.getLocationName());
		}
		message.subject(subject);
		
	    // create the html body
	    HtmlWriter htmlWriter = new HtmlWriter();
	
	    htmlWriter.startDocument();
	    
	    htmlWriter.startDocumentHeader();
	    htmlWriter.documentTitle(subject);
	    htmlWriter.endDocumentHeader();
	    
	    htmlWriter.startDocumentBody();
	    
	    String greeting = I18N.MESSAGES.sitechangeGreeting(recipient.getName());
	    htmlWriter.paragraph(greeting);

	    String intro;
	    if(newSite) {
	    	intro = I18N.MESSAGES.siteCreateIntro(editor.getName(), editor.getEmail(), activityDTO.getName(),
		    		siteDTO.getLocationName(),
		    		userDatabaseDTO.getName(), date);
	    } else {
	    	intro = I18N.MESSAGES.siteChangeIntro(editor.getName(), editor.getEmail(), activityDTO.getName(),
		    		siteDTO.getLocationName(),
		    		userDatabaseDTO.getName(), date);	    
	    }
	    
	    htmlWriter.paragraph(intro);

	    SiteRenderer siteRenderer = new SiteRenderer();
	    siteRenderer.setIndicatorValueFormatter(new IndicatorValueFormatter() {
			@Override
			public String format(Double value) {
				return new DecimalFormat("#,##0.####").format(value);
			}
		});
	    
	    htmlWriter.paragraph(siteRenderer.renderLocation(siteDTO, activityDTO));
	    htmlWriter.paragraph(siteRenderer.renderSite(siteDTO, activityDTO, false, true));

	    String url = "http://www.activityinfo.org/#data-entry/Activity+" + activityDTO.getId();
	    
	    htmlWriter.paragraph("<a href=\"" + url + "\">Open in ActivityInfo</a>");
	    
	    String signature = I18N.MESSAGES.sitechangeSignature();
	    htmlWriter.paragraph(signature);
	
	    htmlWriter.endDocumentBody();
	    htmlWriter.endDocument();
	    
	    LOGGER.fine("Message:\n" + htmlWriter.toString());
	
	    message.htmlBody(htmlWriter.toString());
		
	    return message.build();
	}
}
