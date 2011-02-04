/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.security.Principal;
import java.util.*;


public class MockHttpServletRequest implements HttpServletRequest {

    private HashMap<String, Object> attributes = new HashMap<String, Object>();
    private String contextPath = null;
    private Locale locale = null;
    private HashMap<String, String[]> parameters = new HashMap<String, String[]>();
    private String pathInfo = null;
    private Principal principal = null;
    private String queryString = null;
    private String servletPath = null;
    private HttpSession session = null;
    private String method = null;
    private String contentType = null;
    private ArrayList<Cookie> cookies = new ArrayList<Cookie>(0);
    private String server = "localhost";
    private int port = 8080;
    private boolean secure = false;
    private StringBuffer requestURL;


    public MockHttpServletRequest() {
        super();
    }

    public MockHttpServletRequest(HttpSession session) {
        super();
        setHttpSession(session);
    }

    public void setParameter(String name, String value) {
        parameters.remove(name);
        addParameter(name, value);
    }

    public void addParameter(String name, String value) {
        String[] values = (String[]) parameters.get(name);

        if (values == null) {
            String[] results = new String[]{value};

            parameters.put(name, results);

            return;
        }

        String[] results = new String[values.length + 1];

        System.arraycopy(values, 0, results, 0, values.length);
        results[values.length] = value;
        parameters.put(name, results);
    }

    public void setHttpSession(HttpSession session) {
        this.session = session;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setUserPrincipal(Principal principal) {
        this.principal = principal;
    }

    public String getAuthType() {
        throw new UnsupportedOperationException();
    }

    public String getContextPath() {
        return (contextPath);
    }

    public Cookie[] getCookies() {
        return cookies.toArray(new Cookie[0]);
    }

    public void addCookie(String name, String value) {
        cookies.add(new Cookie(name, value));
    }

    public long getDateHeader(String name) {
        throw new UnsupportedOperationException();
    }

    public String getHeader(String name) {
        throw new UnsupportedOperationException();
    }

    public Enumeration getHeaderNames() {
        throw new UnsupportedOperationException();
    }

    public Enumeration getHeaders(String name) {
        throw new UnsupportedOperationException();
    }

    public int getIntHeader(String name) {
        throw new UnsupportedOperationException();
    }

    public String getMethod() {
        return method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        throw new UnsupportedOperationException();
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRemoteUser() {
        if (principal != null) {
            return (principal.getName());
        } else {
            return (null);
        }
    }

    public String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    }


    public String getRequestURI() {
        StringBuffer sb = new StringBuffer();

        if (contextPath != null) {
            sb.append(contextPath);
        }

        if (servletPath != null) {
            sb.append(servletPath);
        }

        if (pathInfo != null) {
            sb.append(pathInfo);
        }

        return (sb.toString());
    }

    public StringBuffer getRequestURL() {
        if (requestURL == null) {
            throw new IllegalStateException("RequestURL has not been set");
        }

        return requestURL;
    }

    public void setRequestURL(String url) {
        requestURL = new StringBuffer(url);
    }

    public String getServletPath() {
        return servletPath;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    public HttpSession getSession(boolean create) {
        if (create && (session == null)) {
            session = new MockHttpSession();
        }

        return session;
    }

    public Principal getUserPrincipal() {
        return (principal);
    }

    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String role) {
        return false;
    }

    public Object getAttribute(String name) {
        return (attributes.get(name));
    }

    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        return (contentType);
    }

    public ServletInputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    public Locale getLocale() {
        return (locale);
    }

    public Enumeration getLocales() {
        throw new UnsupportedOperationException();
    }

    public String getParameter(String name) {
        String[] values = (String[]) parameters.get(name);

        if (values != null) {
            return (values[0]);
        } else {
            return (null);
        }
    }

    public Map getParameterMap() {
        return (parameters);
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String name) {
        return ((String[]) parameters.get(name));
    }

    public String getProtocol() {
        throw new UnsupportedOperationException();
    }

    public BufferedReader getReader() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    public String getScheme() {
        return "http";
    }

    public String getServerName() {
        return server;
    }

    public int getServerPort() {
        return port;
    }

    public boolean isSecure() {
        return secure;
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }

    public void setCharacterEncoding(String name) {
        throw new UnsupportedOperationException();
    }
}
