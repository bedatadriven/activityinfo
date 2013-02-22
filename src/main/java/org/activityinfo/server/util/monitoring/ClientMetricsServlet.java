package org.activityinfo.server.util.monitoring;

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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

@Singleton
public class ClientMetricsServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ClientMetricsServlet.class
        .getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        new String(ByteStreams.toByteArray(req.getInputStream()));

        // LOGGER.info("UA: " + req.getHeader("User-agent") + "\n" +
        // "Country: " + req.getHeader("CF-IPCountry") + "\n" +
        // body.replace(' ', '\n'));
        //

    }

}
