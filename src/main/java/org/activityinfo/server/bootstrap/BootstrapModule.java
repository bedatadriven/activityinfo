/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.util.Map;

import org.activityinfo.server.bootstrap.jaxrs.FreemarkerViewProcessor;

import com.google.common.collect.Maps;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * The Bootstrap module is responsible for the minimal static
 * html necessary to login, retrieve lost passwords, etc.
 */
public class BootstrapModule extends ServletModule {

    @Override
    protected void configureServlets() {
    	bind(FreemarkerViewProcessor.class);
    	
        serve("/ActivityInfo/ActivityInfo.nocache.js").with(SelectionServlet.class);
        serve("/ActivityInfo/ActivityInfo.appcache").with(SelectionServlet.class);
        serve("/ActivityInfo/ActivityInfo.gears.manifest").with(SelectionServlet.class);
        
        Map<String, String> initParams = Maps.newHashMap();
        initParams.put("swagger.api.basepath", "http://localhost:8888");
        initParams.put("api.version", "1.0");
        filter("/login*").through(GuiceContainer.class);
        
        filterContainer(initParams, HostController.class, LoginController.class, ConfirmInviteController.class, LogoutController.class, ResetPasswordController.class, ChangePasswordController.class);
    }

	private void filterContainer(Map<String, String> params, Class<?>... endpointClasses) {
		for (Class<?> c : endpointClasses) {
			bind(c);
			
			String path = null;
			
			try {
				path = (String) c.getField("ENDPOINT").get(null);
			} catch (Exception exc) {
				throw new RuntimeException(exc);
			}
			
			filter(path).through(GuiceContainer.class, params);
		}
	}
}
