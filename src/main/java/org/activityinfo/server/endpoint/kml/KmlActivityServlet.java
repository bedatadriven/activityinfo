package org.activityinfo.server.endpoint.kml;

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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.authentication.BasicAuthentication;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Singleton
public class KmlActivityServlet extends HttpServlet {

    private final DispatcherSync dispatcher;
    private final BasicAuthentication authenticator;
    private final Configuration templateCfg;
    private final Provider<EntityManager> entityManager;

    @Inject
    public KmlActivityServlet(
        Provider<EntityManager> entityManager,
        Configuration templateCfg,
        BasicAuthentication authenticator,
        DispatcherSync dispatcher) {

        this.templateCfg = templateCfg;
        this.entityManager = entityManager;
        this.authenticator = authenticator;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        // Get Authorization header
        String auth = req.getHeader("Authorization");

        // Do we allow that user?
        User user = authenticator.doAuthentication(auth);
        if (user == null) {
            // Not allowed, or no password provided so report unauthorized
            res.setHeader("WWW-Authenticate",
                "BASIC realm=\"Utilisateurs authorises\"");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String baseURL = "http://" + req.getServerName() + ":"
            + req.getServerPort()
            + "/earth/sites?activityId=";
        SchemaDTO schemaDTO = loadSchema(user);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schema", schemaDTO);
        map.put("baseURL", baseURL);

        Template tpl = templateCfg
            .getTemplate("kml/ActivitiesNetworkLink.kml.ftl");
        res.setContentType("application/vnd.google-earth.kml+xml; filename=ActivityInfo.kml");
        res.setCharacterEncoding("UTF-8");

        try {
            tpl.process(map, res.getWriter());
        } catch (TemplateException e) {
            res.setStatus(500);
            e.printStackTrace();
        }
    }

    private SchemaDTO loadSchema(User user) {
        DomainFilters.applyUserFilter(user, entityManager.get());

        return dispatcher.execute(new GetSchema());
    }
}
