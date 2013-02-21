/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.mail;

import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public class MailModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MailSender.class).to(PostmarkMailSender.class);
    }
    
    public MailSender provideMailSender(DeploymentConfiguration config, Injector injector) {
		return null;
    	
    }
}
