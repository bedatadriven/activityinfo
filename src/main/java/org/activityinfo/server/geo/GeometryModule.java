package org.activityinfo.server.geo;

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
