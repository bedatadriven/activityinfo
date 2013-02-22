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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.google.common.base.Strings;

/**
 * Configuration properties for the 
 * ActivityInfo server that are specified via a property file
 * at deployment time. See {@link ConfigModule} for how
 * these properties files are located.
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
	 * @return a copy of the deployment configuration as a 
	 * java.util.Properties object
	 */
	public Properties asProperties() {
		return (Properties) properties.clone();
	}
	
	/**
	 * Returns the AWS Access Key, trying first {@link BeanstalkEnvironment} 
	 * and then the property {@code aws.accessKey}
	 */
	public String getAwsAccessKeyId() {
		if(BeanstalkEnvironment.credentialsArePresent()) {
			return BeanstalkEnvironment.getAccessKeyId();
		} else {
			return properties.getProperty("aws.accessKeyId");
		}
	}

	/**
	 * Returns the AWS Secret Key, trying first the {@link BeanstalkEnvironment}
	 * and then the property {@code aws.secretKey}
	 */
	public String getAwsSecretKey() {
		if(BeanstalkEnvironment.credentialsArePresent()) {
			return BeanstalkEnvironment.getSecretKey();
		} else {
			return properties.getProperty("aws.secretKey");
		}
	}
	
	public AWSCredentials getAWSCredentials() {
		return new BasicAWSCredentials(getAwsAccessKeyId(), getAwsSecretKey());
	}

	public boolean hasProperty(String key) {
		return !Strings.isNullOrEmpty(getProperty(key));
	}
}
