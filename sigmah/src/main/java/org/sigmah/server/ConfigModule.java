/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.sigmah.server.util.logging.Trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigModule extends AbstractModule {
    private static Logger logger = Logger.getLogger(ConfigModule.class);

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Trace
    public Properties provideConfigProperties() {
        Properties properties = new Properties();
        tryToLoadFromTomcatConfDirectory(properties);
        return properties;
    }

    private boolean tryToLoadFromTomcatConfDirectory(Properties properties) {
        try {
            File appConfig = new File("conf" + File.separator + "activityinfo.conf");
            logger.info("Trying to read properties from tomcat conf folder: " + appConfig.getAbsolutePath());
            if(appConfig.exists()) {
                logger.info("Reading properties from " + appConfig.getAbsolutePath());
                properties.load(new FileInputStream(appConfig));
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
