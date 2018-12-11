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

import java.util.Properties;

import com.google.common.base.Strings;

/**
 * Configuration properties for the ActivityInfo server that are specified via a
 * property file at deployment time. See {@link ConfigModule} for how these
 * properties files are located.
 */
public class DeploymentConfiguration {

    private final Properties properties;

    public DeploymentConfiguration(Properties properties) {
        super();
        this.properties = properties;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 
     * @return a copy of the deployment configuration as a java.util.Properties
     *         object
     */
    public Properties asProperties() {
        return (Properties) properties.clone();
    }

    public boolean hasProperty(String key) {
        return !Strings.isNullOrEmpty(getProperty(key));
    }
}
