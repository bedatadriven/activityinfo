package org.activityinfo.server.util.config;

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

import com.google.common.base.Strings;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.activityinfo.server.util.logging.Trace;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Guice module that provides the {@link DeploymentConfiguration} used across
 * the server side.
 * 
 */
public class ConfigModule extends ServletModule {
    private static Logger logger = Logger.getLogger(ConfigModule.class
        .getName());

    @Override
    protected void configureServlets() {
    }

    @Provides
    @Singleton
    @Trace
    public DeploymentConfiguration provideDeploymentConfig(
        ServletContext context) {
        Properties properties = new Properties();

        tryToLoadFrom(properties, webInfDirectory(context));
        tryToLoadFrom(properties, systemSettings());
        tryToLoadFrom(properties, userSettings());

        // specified at server start up with
        // -Dactivityinfo.config=/path/to/conf.properties
        if (!Strings.isNullOrEmpty(System.getProperty("activityinfo.config"))) {
            tryToLoadFrom(properties,
                new File(System.getProperty("activityinfo.config")));
        }

        return new DeploymentConfiguration(properties);
    }

    private boolean tryToLoadFrom(Properties properties, File file) {
        try {
            logger.info("Trying to read properties from: "
                + file.getAbsolutePath());
            if (file.exists()) {
                logger
                    .info("Reading properties from " + file.getAbsolutePath());
                properties.load(new FileInputStream(file));
                return true;
            }
        } catch (IOException e) {
            return false;
        } catch (AccessControlException e) {
            return false;
        }
        return false;
    }

    private File webInfDirectory(ServletContext context) {
        return new File(context.getRealPath("WEB-INF") + File.separator
            + "activityinfo.properties");
    }
    
    private File systemSettings() {
        return new File("/etc/activityinfo.properties");
    }

    private File userSettings() {
        return new File(System.getProperty("user.home") + File.separator
            + "activityinfo.properties");
    }
}
