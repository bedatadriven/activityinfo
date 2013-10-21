package org.activityinfo.server.endpoint.export;

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
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.FilterUrlSerializer;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Exports complete data to an Excel file
 * 
 * @author Alex Bertram
 */
@Singleton
public class ExportSitesServlet extends HttpServlet {
    private DispatcherSync dispatcher;

    @Inject
    public ExportSitesServlet(DispatcherSync dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        Set<Integer> activities = new HashSet<Integer>();
        if (req.getParameterValues("a") != null) {
            for (String activity : req.getParameterValues("a")) {
                activities.add(Integer.parseInt(activity));
            }
        }

        Filter filter = FilterUrlSerializer.fromQueryParameter(req
            .getParameter("filter"));

        SchemaDTO schema = dispatcher.execute(new GetSchema());

        SiteExporter export = new SiteExporter(dispatcher);
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                if (!filter.isRestricted(DimensionType.Activity)
                    ||
                    filter.getRestrictions(DimensionType.Activity).contains(
                        activity.getId())) {
                    export.export(activity, filter);
                
                }
            }
        }
        export.done();

        resp.setContentType("application/vnd.ms-excel");
        if (req.getHeader("User-Agent").indexOf("MSIE") != -1) {
            resp.addHeader("Content-Disposition",
                "attachment; filename=ActivityInfo.xls");
        } else {
            resp.addHeader(
                "Content-Disposition",
                "attachment; filename="
                    +
                    ("ActivityInfo Export " + new Date().toString() + ".xls")
                        .replace(" ", "_"));
        }

        OutputStream os = resp.getOutputStream();
        export.getBook().write(os);
    }
}