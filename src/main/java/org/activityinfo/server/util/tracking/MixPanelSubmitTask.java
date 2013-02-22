package org.activityinfo.server.util.tracking;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mixpanel.mixpanelapi.MixpanelAPI;

@Singleton
public class MixPanelSubmitTask extends HttpServlet {

    public static final String END_POINT = "/tasks/mixpanel/submit";

    private final MixpanelAPI mixpanel = new MixpanelAPI();

    private static final Logger LOGGER = Logger
        .getLogger(MixPanelSubmitTask.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        try {
            mixpanel.sendMessage(new JSONObject(req.getParameter("message")));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to submit MixPanel message:\n"
                + req.getParameter("message"), e);
        }
    }

}
