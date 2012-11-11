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
public class MozillaAppController extends AbstractController {

	public static final String ENDPOINT = "/desktop";
	private DeploymentConfiguration deployConfig;

	@Inject
	public MozillaAppController(Injector injector,
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
		model.setMapsApiKey(deployConfig.getProperty("mapsApiKey"));
		writeView(resp, model);
	}	
}
