package org.activityinfo.server.endpoint.hxl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

@Provider
@Singleton
public class ConfigProvider extends SingletonTypeInjectableProvider<Context, ServletConfig> {

	
	@Inject
	public ConfigProvider(ServletContext servletContext) {
		super(ServletConfig.class, new Config(servletContext));
	}

	private static class Config implements ServletConfig {

		private Map<String, String> initParams = Maps.newHashMap();
		private ServletContext servletContext;
		
		public Config(ServletContext servletContext) {
			this.servletContext = servletContext;
		}

		@Override
		public String getInitParameter(String key) {
			return initParams.get(key);
		}

		@Override
		public Enumeration getInitParameterNames() {
			return Collections.enumeration(initParams.keySet());
		}

		@Override
		public ServletContext getServletContext() {
			return servletContext;
		}

		@Override
		public String getServletName() {
			return "ActivityInfo";
		}
		
	}
	
}
