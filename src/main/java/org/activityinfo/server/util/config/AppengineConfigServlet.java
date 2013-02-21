package org.activityinfo.server.util.config;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Simple servlet to allow AppEngine administrators to define the 
 * configuration properties for this instance. This makes it possible
 * to set config params, like api keys, etc, seperately from the (public)
 * source code.
 * 
 * <p>This servlet stores the text of a properties file to the Datastore
 *
 */
@Singleton
public class AppengineConfigServlet extends HttpServlet {

	public static final String END_POINT = "/admin/config";
	
	private final Configuration templateCfg;
	
	@Inject
	public AppengineConfigServlet(Configuration templateCfg) {
		super();
		this.templateCfg = templateCfg;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		Map<String, String> model = Maps.newHashMap();
		model.put("currentConfig", AppEngineConfig.getPropertyFile());
		
		Template template = templateCfg.getTemplate("/page/Config.ftl");
		try {
			template.process(model, resp.getWriter());
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String config = req.getParameter("config");
		AppEngineConfig.setPropertyFile(config);
		
		resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
		resp.setHeader("Location", req.getRequestURI());
	}	
}
