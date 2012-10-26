package org.activityinfo.server.geo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import freemarker.log.Logger;

@Singleton
public class LocalGeometryStorage implements GeometryStorage {
	
	private static final Logger LOGGER = Logger.getLogger(LocalGeometryStorage.class.getName());
	private File geoRoot = new File("");
	
	@Inject
	public LocalGeometryStorage(DeploymentConfiguration config) {
		String geoRootPath = config.getProperty("geometry.rootdir");
		if(Strings.isNullOrEmpty(geoRootPath)) {
			LOGGER.error("The property 'geometry.rootdir' has not been set: polygons will not rendered");
		} else {
			this.geoRoot = new File(geoRootPath);
			if(!this.geoRoot.exists()) { 
				LOGGER.error("'geometry.rootdir' does not exist: " + geoRootPath);
			}
		}	
	}

	@Override
	public InputStream openWkb(int adminLevelId) throws IOException {
		return new FileInputStream(new File(geoRoot, adminLevelId + ".wkb" ));
	}

	@Override
	public void serveJson(int adminLevelId, boolean gzip, HttpServletResponse response) throws IOException {
		File jsonFile = new File(geoRoot, adminLevelId + ".json");
		Files.copy(jsonFile, response.getOutputStream());
	}
}
