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

package org.activityinfo;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import org.activityinfo.test.TestScoped;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.testing.ServletTester;
import org.mortbay.log.Log;
import org.mortbay.log.StdErrLog;

public class ServletTesterModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @TestScoped
    public ServletTester provideTester(final Injector injector) throws Exception {
        StdErrLog log = new StdErrLog();
        log.setDebugEnabled(true);
        Log.setLog(log);

        ServletTester tester = new ServletTester();
        tester.setContextPath("/context");
        tester.addFilter(GuiceFilter.class, "/*", Handler.ALL);
        tester.addServlet(DefaultServlet.class, "/");
        tester.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return injector;
            }
        });
        return tester;
    }
}
