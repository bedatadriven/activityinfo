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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;

import com.sun.jersey.api.view.Viewable;
import com.vividsolutions.jts.geom.Geometry;

public class AdminEntityResource {

    private AdminEntity entity;

    public AdminEntityResource(AdminEntity unit) {
        super();
        this.entity = unit;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable get() {
        return new Viewable("/resource/AdminEntity.ftl", entity);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/geometry")
    public Geometry getGeometry() {
        return entity.getGeometry();
    }

}
