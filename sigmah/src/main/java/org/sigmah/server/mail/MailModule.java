/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import com.google.inject.AbstractModule;

public class MailModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MailSender.class).to(MailSenderImpl.class);
    }
}
