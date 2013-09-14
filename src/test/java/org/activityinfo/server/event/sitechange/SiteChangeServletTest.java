package org.activityinfo.server.event.sitechange;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.MailSenderStub;
import org.activityinfo.server.mail.MailSenderStubModule;
import org.activityinfo.server.mail.Message;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
@Modules({ MailSenderStubModule.class })
public class SiteChangeServletTest extends CommandTestCase2 {
    @Inject
    private EntityManager entityManager;

    @Inject
    private MailSender mailSender;

    @Test
    public void testSubscriptions() throws Exception {
        SiteChangeServlet underTest = createServlet();

        List<User> users = underTest.findRecipients(1);
        assertThat(users.size(), is(equalTo(2)));

        User alex = users.get(0);
        assertThat(alex.getName(), is(equalTo("Alex")));
        assertThat(alex.isEmailNotification(), is(true));
        assertThat(alex.getLocale(), is(equalTo("fr")));

        User marlene = users.get(1);
        assertThat(marlene.getName(), is(equalTo("Marlene")));
        assertThat(marlene.isEmailNotification(), is(true));
        assertThat(marlene.getLocale(), is(equalTo("en")));
    }

    @Test
    public void testSendNotifications() throws Exception {
        SiteChangeServlet underTest = createServlet();
        underTest.sendNotifications(1, 1, ChangeType.CREATE);

        List<Message> msgs = ((MailSenderStub) mailSender).sentMails;
        assertThat(msgs.size(), is(equalTo(1)));

        Message msgToMarlene = msgs.get(0);
        assertThat(msgToMarlene.getTo().get(0).toString(),
            is(equalTo("Marlene <marlene@solidarites>")));
        assertTrue(msgToMarlene.hasHtmlBody());
        assertThat(msgToMarlene.getSubject(),
            is(equalTo("PEAR: New NFI at Penekusu Kivu by NRC")));
    }

    private Matcher<String> startsWith(final String expected) {
        return new TypeSafeMatcher<String>(String.class) {

            @Override
            public void describeTo(Description d) {
                d.appendText("starts with ");
                d.appendValue(expected);
            }

            @Override
            public boolean matchesSafely(String item) {
                return item.startsWith(expected);
            }

        };
    }

    private SiteChangeServlet createServlet() {
        return new SiteChangeServlet(
            new Provider<EntityManager>() {
                @Override
                public EntityManager get() {
                    return entityManager;
                }
            },
            new Provider<MailSender>() {
                @Override
                public MailSender get() {
                    return mailSender;
                }
            },
            new ServerSideAuthProvider(),
            getDispatcherSync());
    }

    public static void main(String[] args) {
    }
}