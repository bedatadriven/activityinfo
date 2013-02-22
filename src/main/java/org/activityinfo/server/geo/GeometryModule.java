package org.activityinfo.server.geo;

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

import org.activityinfo.server.DeploymentEnvironment;

import com.google.inject.servlet.ServletModule;

public class GeometryModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(AdminGeometryProvider.class).to(WkbGeometryProvider.class);
		serve(GeometryServlet.END_POINT).with(GeometryServlet.class);
		
		if(DeploymentEnvironment.isAppEngineProduction()) {
			bind(GeometryStorage.class).to(GcsGeometryStorage.class);
		} else if(DeploymentEnvironment.isAppEngine()){
			bind(GeometryStorage.class).to(RemoteGcsStorage.class);
		} else {
			bind(GeometryStorage.class).to(LocalGeometryStorage.class);
		}
	}
}
