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

package org.activityinfo.server.endpoint.jaxws;

import com.google.inject.Inject;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.exception.InvalidLoginException;
import org.jvnet.jax_ws_commons.json.JSONBindingID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * A Façade that exposes the server's Commands to clients as Jax-WS service. Clients
 * can access the service through SOAP (easy for .NET clients) or through JSON from
 * a web page.
 *
 * @author Alex Bertram
 */
@WebService
@GuiceManaged
@BindingType(JSONBindingID.JSON_BINDING)
public class ApiService {

    @Inject
    private Authenticator authenticator;

    /**
     * Attempts to authenticate the user and returns a secure token that can be used to
     * access the API.
     *
     * @param email the user's email
     * @param password the user's password in plain text
     * @return a secure authentication token, or null if the authentication fails.
     */
    @WebMethod
    public String authenticate(
            @WebParam(name="email") String email,
            @WebParam(name="password") String password) {
        try {
            return authenticator.authenticate(email, password).getId();
        } catch (InvalidLoginException e) {
            return null;
        }
    }
}
