package org.activityinfo.server.endpoint.hxl;

import org.activityinfo.server.bootstrap.jaxrs.RestModule;

import com.wordnik.swagger.jaxrs.JaxrsApiReader;

public class HxlModule extends RestModule {

	@Override
	protected void configureResources() {
		
		configureSwagger();

		bindResource(HxlResources.class);
		bind(ConfigProvider.class);
		bindResource(ApiListingResource.class);
		bindResource(CountryResource.class);
		
	}

	private void configureSwagger() {
		// disable .json and .xml suffixes on rest resources
		// ick!
	    JaxrsApiReader.setFormatString("");
	}

	
}
