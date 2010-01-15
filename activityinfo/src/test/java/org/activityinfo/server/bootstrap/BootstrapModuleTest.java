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

package org.activityinfo.server.bootstrap;

import com.google.inject.Inject;
import org.activityinfo.ServletTesterModule;
import org.activityinfo.server.TemplateModule;
import org.activityinfo.server.auth.AuthenticationModule;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.dao.ServletTestingDataModule;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(InjectionSupport.class)
@Modules({ServletTestingDataModule.class,
        AuthenticationModule.class,
        TemplateModule.class,
        BootstrapModule.class,
        ServletTesterModule.class})
public class BootstrapModuleTest {

    @Inject
    private ServletTester tester;
    @Inject
    private HttpTester request;
    @Inject
    private HttpTester response;

    @Before
    public void setUp() throws Exception {
        tester.start();
        request.setHeader("Host", "tester");
        request.setMethod("GET");
        request.setVersion("HTTP/1.0");
    }

    @Test
    public void testLoginPage() throws Exception {
        request.setURI("/context/login");
        response.parse(tester.getResponses(request.generate()));

        assertNotNull("content not present", response.getContent());
        assertEquals(200, this.response.getStatus());

        System.err.println(response.getContent());
    }

    @Test
    @OnDataSet("/dbunit/authTest.db.xml")
    public void testLoginPost() throws Exception {
        request.setURI("/context/login");
        request.setMethod("POST");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setContent("email=stefan@irc.org&password=tuesday");

        String s = tester.getResponses(request.generate());
        response.parse(s);

        assertEquals(302, response.getStatus());
        assertEquals("http://tester/context", response.getHeader("Location"));
    }

    @Test
    public void testConfirmInvite() throws Exception {
        request.setURI("/context/confirmInvite?XYBZT");
        response.parse(tester.getResponses(request.generate()));

        System.err.print(response.getContent());

        assertNotNull("content not present", response.getContent());
        assertEquals(200, this.response.getStatus());
    }

    @After
    public void tearDown() throws Exception {
        tester.stop();
    }

}
