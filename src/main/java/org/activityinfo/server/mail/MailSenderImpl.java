/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;

import freemarker.template.Configuration;

public class MailSenderImpl extends MailSender {
	
	private final DeploymentConfiguration configuration;
	
	@Inject	
    public MailSenderImpl(DeploymentConfiguration configuration, Configuration templateCfg) {
		super(templateCfg);
		this.configuration = configuration;
	}

    
	@Override
	@LogException
    public void send(Message message) {
		try {
			message.setFrom(new InternetAddress(
	        		configuration.getProperty("smtp.from"), 
	        		configuration.getProperty("smtp.from.name")));
	     
	        Transport.send(message);
		} catch(MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
    }
}
