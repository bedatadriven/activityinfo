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
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.dao;

import org.activityinfo.server.dao.jpa.AuthDAOJPA;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.server.service.PasswordGenerator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import static org.easymock.EasyMock.*;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;

import freemarker.template.Configuration;

import java.io.File;

/**
 * @author Alex Bertram
 */
public class CreateUserTest {


    @Test
    public void testCreate() throws Throwable {

        // Dependency: EntityManager
        EntityManager em = createNiceMock(EntityManager.class);
        replay(em);

        // Dependency: Mailer
        Mailer mailer = new Mailer() {
            @Override
            public void send(Email message) throws EmailException {
                message.setFrom("test@test.org");
                message.setHostName("localhost");
                message.buildMimeMessage();
                try {
                    String msg = (String) message.getMimeMessage().getContent();
                    System.out.println(msg);
                    Assert.assertTrue("password is present", msg.indexOf("foobar")>=0);
                    
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        };

        // Dependency: Password Generator
        PasswordGenerator pgen = createMock(PasswordGenerator.class);
        expect(pgen.generate()).andReturn("foobar");
        replay(pgen);

        // Template configruation
        Configuration templateCfg = new Configuration();
        templateCfg.setDirectoryForTemplateLoading(new File("war/ftl"));
        templateCfg.setDefaultEncoding("UTF-8");

        // Class under Test
        AuthDAO authDAO = new AuthDAOJPA(em, templateCfg, pgen, mailer);
        authDAO.createUser("jbisimbwa@gmail.com", "Jean", "fr", new User("akbertram@gmail.com", "Alex", "fr") );


        



    }

}
