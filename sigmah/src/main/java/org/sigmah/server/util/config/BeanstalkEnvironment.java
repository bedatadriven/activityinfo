package org.sigmah.server.util.config;

import com.google.common.base.Strings;

/**
 * These properties are provided by the Amazon Beanstalk container
 * 
 */
final class BeanstalkEnvironment {


	private BeanstalkEnvironment() { }
	
	/**
	 * Returns the {@code AWS_ACCESS_KEY_ID} property 
	 * set by the Beanstalk container.
	 */
	public static String getAccessKeyId() {
		return System.getProperty("AWS_ACCESS_KEY_ID");
	}
	
	/**
	 * 
	 * Returns the {@code AWS_SECRET_KEY} property set by the 
	 * Beanstalk container.
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
	 * Returns the name of the S3 Bucket where the configuration file 
	 * for this deployment is stored. (Expected in Beanstalk variable {@code PARAM1})
	 */
	public static String getConfigurationPropertiesBucket() {
		return System.getProperty("PARAM1");
	}
	
	/**
	 * Returns the key of the configuration file for this deployment. 
	 * (Expected in Beanstalk environment variable {@code PARAM2})
	 */
	public static String getConfigurationPropertiesKey() {
		return System.getProperty("PARAM2");
	}

}
