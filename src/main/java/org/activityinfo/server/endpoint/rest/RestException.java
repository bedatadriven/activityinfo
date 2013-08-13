package org.activityinfo.server.endpoint.rest;


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


public class RestException extends RuntimeException {
    private static final long serialVersionUID = -7928542635378858495L;

    public RestException() {
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(String endpoint, String message) {
        this(createMessage(endpoint, message));
    }

    public RestException(Throwable e) {
        super(e);
    }

    public RestException(String message, Throwable e) {
        super(message, e);
    }

    public RestException(String endpoint, String message, Throwable e) {
        this(createMessage(endpoint, message), e);
    }

    private static String createMessage(String endpoint, String message) {
        return endpoint + " - " + message;
    }

}
