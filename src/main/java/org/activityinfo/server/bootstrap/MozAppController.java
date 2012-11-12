package org.activityinfo.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import freemarker.template.Configuration;

@Singleton
public class MozAppController extends AbstractController {

	public static final String ENDPOINT = "/moz";
	private DeploymentConfiguration deployConfig;

	@Inject
	public MozAppController(Injector injector,
			Configuration templateConfig, DeploymentConfiguration deployConfig) {
		super(injector, templateConfig);
		this.deployConfig = deployConfig;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		HostPageModel model = new HostPageModel();
		model.setAppUrl(HostPageModel.computeAppUrl(req));
		model.setAppCacheEnabled(true);
		model.setModuleName("ActivityInfoMozApp");
		model.setMapsApiKey(deployConfig.getProperty("mapsApiKey"));
		
		writeView(resp, model);
	}
}
