package org.activityinfo.server.endpoint.hxl;

import org.activityinfo.server.bootstrap.jaxrs.RestModule;

public class HxlModule extends RestModule {

	@Override
	protected void configureResources() {
		bindResource(HxlResources.class);
	}


	
}
