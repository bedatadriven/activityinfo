/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.sigmah.server.util.logging.LogException;

import com.google.inject.Inject;

public class MailSenderImpl implements MailSender {
	
	private Properties configuration;
	
	@Inject	
    public MailSenderImpl(Properties configuration) {
		super();
		this.configuration = configuration;
	}


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
