package org.activityinfo.server;

import com.google.appengine.api.utils.SystemProperty;


public class DeploymentEnvironment {
	
	public static boolean isAppEngine() {
		return true;
	}
	
	public static boolean isAppEngineProduction() {
		return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
	}

	public static boolean isAppEngineDevelopment() {
		return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;

	}

}
