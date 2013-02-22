package org.activityinfo.server.geo;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import freemarker.log.Logger;

@Singleton
public class LocalGeometryStorage implements GeometryStorage {

    private static final Logger LOGGER = Logger
        .getLogger(LocalGeometryStorage.class.getName());
    private File geoRoot = new File("");

    @Inject
    public LocalGeometryStorage(DeploymentConfiguration config) {
        String geoRootPath = config.getProperty("geometry.rootdir");
        if (Strings.isNullOrEmpty(geoRootPath)) {
            LOGGER
                .error("The property 'geometry.rootdir' has not been set: polygons will not rendered");
        } else {
            this.geoRoot = new File(geoRootPath);
            if (!this.geoRoot.exists()) {
                LOGGER.error("'geometry.rootdir' does not exist: "
                    + geoRootPath);
            }
        }
    }

    public LocalGeometryStorage(File georoot) {
        this.geoRoot = georoot;
    }

    @Override
    public InputStream openWkb(int adminLevelId) throws IOException {
        return new GZIPInputStream(
            new FileInputStream(new File(geoRoot, adminLevelId + ".wkb.gz")));
    }

    @Override
    public void serveJson(int adminLevelId, boolean gzip,
        HttpServletResponse response) throws IOException {
        File jsonFile = new File(geoRoot, adminLevelId + ".json");
        Files.copy(jsonFile, response.getOutputStream());
    }
}
