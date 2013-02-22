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

/**
 * These properties are provided by the Amazon Beanstalk container
 * 
 */
final class BeanstalkEnvironment {

    private BeanstalkEnvironment() {
    }

    /**
     * Returns the {@code AWS_ACCESS_KEY_ID} property set by the Beanstalk
     * container.
     */
    public static String getAccessKeyId() {
        return System.getProperty("AWS_ACCESS_KEY_ID");
    }

    /**
     * 
     * Returns the {@code AWS_SECRET_KEY} property set by the Beanstalk
     * container.
     */
    public static String getSecretKey() {
        return System.getProperty("AWS_SECRET_KEY");
    }

    public static boolean credentialsArePresent() {
        return !Strings.isNullOrEmpty(getAccessKeyId()) &&
            !Strings.isNullOrEmpty(getSecretKey());
    }

    /**
     * 
     * Returns the name of the S3 Bucket where the configuration file for this
     * deployment is stored. (Expected in Beanstalk variable {@code PARAM1})
     */
    public static String getConfigurationPropertiesBucket() {
        return System.getProperty("PARAM1");
    }

    /**
     * Returns the key of the configuration file for this deployment. (Expected
     * in Beanstalk environment variable {@code PARAM2})
     */
    public static String getConfigurationPropertiesKey() {
        return System.getProperty("PARAM2");
    }

}
