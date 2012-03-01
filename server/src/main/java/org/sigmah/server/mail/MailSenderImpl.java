/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.sigmah.server.i18n.LocaleHelper;
import org.sigmah.server.util.config.DeploymentConfiguration;
import org.sigmah.server.util.logging.LogException;
import org.sigmah.server.util.logging.Trace;

import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MailSenderImpl implements MailSender {
	
	private final DeploymentConfiguration configuration;
	private final Configuration templateCfg;
	
	@Inject	
    public MailSenderImpl(DeploymentConfiguration configuration, Configuration templateCfg) {
		super();
		this.configuration = configuration;
		this.templateCfg = templateCfg;
	}

    @Override
	@Trace
    @LogException
    public void send(MailMessage message) {
    	try {
	        SimpleEmail mail = new SimpleEmail();
	        mail.addTo(message.getRecipient().getEmail(), message.getRecipient().getName());
	        mail.addBcc("akbertram@gmail.com"); // for testing purposes
	        mail.setSubject(getSubject(message));
	        mail.setMsg(composeMessage(message));
	
	        send(mail);
    	} catch(Exception e) {
    		throw new RuntimeException("Exception while sending message: " + e.getMessage(), e);
    	}
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
        return ResourceBundle.getBundle("org.sigmah.server.mail.MailMessages", 
        		LocaleHelper.getLocaleObject(message.getRecipient()));
    }

	@Override
	@LogException
    public void send(Email email) throws EmailException {
        email.setHostName(configuration.getProperty("smtp.host"));
        email.setFrom(configuration.getProperty("smtp.from"), 
        		configuration.getProperty("smtp.from.name"));
        if(configuration.getProperty("smtp.port") != null ) {
        	email.setSmtpPort(Integer.parseInt(configuration.getProperty("smtp.port")));
        }
        if(configuration.getProperty("smtp.username") != null && configuration.getProperty("smtp.password") != null) {
        	email.setAuthenticator(new DefaultAuthenticator(configuration.getProperty("smtp.username"), 
        			configuration.getProperty("smtp.password")));
        	
        }
        if(configuration.getProperty("smtp.tls")!=null) {
        	email.setTLS(true);
        }
        
        email.send();
    }
}
