package org.activityinfo.test;

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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

public class ServletStubModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public ServletContext providesServletContext()
    {
        return new ServletContext() {

            @Override
            public void setAttribute(String arg0, Object arg1) {

            }

            @Override
            public void removeAttribute(String arg0) {

            }

            @Override
            public void log(String arg0, Throwable arg1) {

            }

            @Override
            public void log(Exception arg0, String arg1) {

            }

            @Override
            public void log(String arg0) {

            }

            @Override
            public Enumeration getServlets() {
                throw new RuntimeException();
            }

            @Override
            public Enumeration getServletNames() {
                throw new RuntimeException();
            }

            @Override
            public String getServletContextName() {
                return "StubServlet";
            }

            @Override
            public Servlet getServlet(String arg0) throws ServletException {
                return null;
            }

            @Override
            public String getServerInfo() {
                return null;
            }

            @Override
            public Set getResourcePaths(String arg0) {
                return null;
            }

            @Override
            public InputStream getResourceAsStream(String arg0) {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public URL getResource(String arg0) throws MalformedURLException {
                throw new RuntimeException();
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String arg0) {
                throw new RuntimeException();
            }

            @Override
            public String getRealPath(String arg0) {
                throw new RuntimeException();
            }

            @Override
            public RequestDispatcher getNamedDispatcher(String arg0) {
                throw new RuntimeException();
            }

            @Override
            public int getMinorVersion() {
                throw new RuntimeException();
            }

            @Override
            public String getMimeType(String arg0) {
                throw new RuntimeException();
            }

            @Override
            public int getMajorVersion() {
                throw new RuntimeException();
            }

            @Override
            public Enumeration getInitParameterNames() {
                throw new RuntimeException();
            }

            @Override
            public String getInitParameter(String arg0) {
                throw new RuntimeException();
            }

            @Override
            public String getContextPath() {
                throw new RuntimeException();
            }

            @Override
            public ServletContext getContext(String arg0) {
                throw new RuntimeException();
            }

            @Override
            public Enumeration getAttributeNames() {
                throw new RuntimeException();
            }

            @Override
            public Object getAttribute(String arg0) {
                return null;
            }

        };
    }
}
