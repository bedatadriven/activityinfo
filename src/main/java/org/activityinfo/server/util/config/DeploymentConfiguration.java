package org.activityinfo.server.util.config;

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
