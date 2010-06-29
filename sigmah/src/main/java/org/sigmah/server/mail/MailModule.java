/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class MailModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MailSender.class).to(MailSenderImpl.class);
        bind(new TypeLiteral<Mailer<Invitation>>() {}).to(InvitationMailer.class);
    }
}
