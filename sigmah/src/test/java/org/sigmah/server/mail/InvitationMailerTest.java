/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sigmah.shared.domain.User;
import org.sigmah.server.dao.hibernate.UserDAOImpl;
import org.sigmah.server.util.TemplateModule;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static junit.framework.Assert.assertTrue;

@RunWith(Parameterized.class)
public class InvitationMailerTest {

    @Parameterized.Parameters
    public static Collection<Object[]> locales() {
        return Arrays.asList(new Object[][] { {Locale.ENGLISH}, { Locale.FRENCH }});
    }

    private Locale locale;

    public InvitationMailerTest(Locale locale) {
        this.locale = locale;
    }

    @Test
    public void testExecutes() throws IOException, TemplateException, EmailException {
    	
    	
        User newUser = UserDAOImpl.createNewUser("invitee@gmail.com", "Invitee", "en");
        User invitingUser = UserDAOImpl.createNewUser("akbertram@gmail.com", "Inviter", "en");

        Invitation invitation = new Invitation(newUser, invitingUser);

        TemplateModule templateModule = new TemplateModule();
        Configuration templateCfg = templateModule.provideConfiguration();

        MockMailSender sender = new MockMailSender();
        InvitationMailer mailer = new InvitationMailer(templateCfg, sender);

        mailer.send(invitation, locale);

        assertTrue(!sender.sentMail.isEmpty());
    }
}
