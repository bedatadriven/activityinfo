/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server.mail;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sigmah.server.domain.User;
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

        User newUser = User.createNewUser("invitee@gmail.com", "Invitee", "en");
        User invitingUser = User.createNewUser("akbertram@gmail.com", "Inviter", "en");

        Invitation invitation = new Invitation(newUser, invitingUser);

        TemplateModule templateModule = new TemplateModule();
        Configuration templateCfg = templateModule.provideConfiguration();

        MockMailSender sender = new MockMailSender();
        InvitationMailer mailer = new InvitationMailer(templateCfg, sender);

        mailer.send(invitation, locale);

        assertTrue(!sender.sentMail.isEmpty());
    }
}
