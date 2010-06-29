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

package org.sigmah.server.mock;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Alex Bertram
 */
public class MockHttpServletResponse implements HttpServletResponse {

    public ArrayList<Cookie> cookies = new ArrayList<Cookie>();
    public Map<String, String> headers = new HashMap<String, String>();
    public int statusCode = 200;
    public String errorMessage;
    public String redirectUrl;
    public String contentType;
    public String characterEncoding;
    public int contentLength;
    public MockServletOutputStream os = new MockServletOutputStream();


    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String s) {
        return headers.keySet().contains(s);
    }

    @Override
    public String encodeURL(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String encodeRedirectURL(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public String encodeUrl(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public String encodeRedirectUrl(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendError(int statusCode, String message) throws IOException {
        this.statusCode = statusCode;
        this.errorMessage = message;
    }

    @Override
    public void sendError(int statusCode) throws IOException {
        this.statusCode = statusCode;
    }

    @Override
    public void sendRedirect(String url) throws IOException {
        this.redirectUrl = url;
    }

    @Override
    public void setDateHeader(String s, long l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDateHeader(String s, long l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeader(String name, String content) {
        headers.put(name, content);
    }

    @Override
    public void addHeader(String name, String content) {
        setHeader(name, content);
    }

    @Override
    public void setIntHeader(String name, int value) {
        headers.put(name, Integer.toString(value));
    }

    @Override
    public void addIntHeader(String name, int value) {
        setIntHeader(name, value);
    }

    @Override
    public void setStatus(int code) {
        this.statusCode = code;
    }

    @Override
    @Deprecated
    public void setStatus(int i, String s) {
        this.statusCode = i;
        this.errorMessage = s;
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return os;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(os);
    }

    @Override
    public void setCharacterEncoding(String s) {
        this.characterEncoding = s;
    }

    @Override
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    public String getCookie(String name) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
