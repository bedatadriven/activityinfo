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

package org.activityinfo.server.servlet;

import com.google.inject.Injector;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.net.URLEncoder;

import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.exception.CommandException;

/**
 *
 * Exports complete data to an Excel file
 *
 * @author Alex Bertram
 */
@Singleton
public class ExportServlet extends HttpServlet {


    private Injector injector;

    @Inject
    public ExportServlet(Injector injector) {
        this.injector = injector;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthDAO authDAO = injector.getInstance(AuthDAO.class);
        Authentication auth = authDAO.getSession(req.getParameter("auth"));

        if(auth == null) {
            // todo: offer basic authentication?
            resp.setStatus(500);
            return;
        }

        Set<Integer> activities = new HashSet<Integer>();
        for(String activity : req.getParameterValues("a")) {
            activities.add(Integer.parseInt(activity));
        }

        try {

            DomainFilters.applyUserFilter(auth.getUser(), injector.getInstance(EntityManager.class));

            Schema schema = HandlerUtil.execute(injector, new GetSchema(), auth.getUser());
            SiteTableDAO siteDAO = injector.getInstance(SiteTableDAO.class);

            Export export = new Export(auth.getUser(), siteDAO);
            for(UserDatabaseDTO db : schema.getDatabases()) {
                for(ActivityModel activity : db.getActivities()) {
                    if(activities.size() == 0 || activities.contains(activity.getId())) {
                        export.export(activity);
                    }
                }
            }

            resp.setContentType("application/vnd.ms-excel");
            resp.addHeader("Content-Disposition", "attachment; filename=" +
                    URLEncoder.encode("ActivityInfo Export " + new Date().toString(), "UTF-8"));

            OutputStream os = resp.getOutputStream();
            export.getBook().write(os);


        } catch (CommandException e) {
            e.printStackTrace();
            resp.setStatus(500);
        }

    }



}