package org.activityinfo.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

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
