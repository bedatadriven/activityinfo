/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.sigmah.server.util.logging.Trace;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ConfigModule extends AbstractModule {
    private static Logger logger = Logger.getLogger(ConfigModule.class);

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Trace
    public Properties provideConfigProperties(ServletContext context) {
        Properties properties = new Properties();

        tryToLoadFrom(properties, webInfDirectory(context));
        tryToLoadFrom(properties, tomcatConfigurationDirectory());
        tryToLoadFromS3(properties);

        return properties;
    }

    private boolean tryToLoadFrom(Properties properties, File file) {
        try {
            logger.info("Trying to read properties from: " + file.getAbsolutePath());
            if(file.exists()) {
                logger.info("Reading properties from " + file.getAbsolutePath());
                properties.load(new FileInputStream(file));
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    /** 
     * 
     * Tries to load a configuration file from an external object stored in Amazon S3.
     * 
     * This is very useful in automating production and continuous deployment scenarios
     * because it allows us to keep the config and the app artifact seperate. 
     * 
     * This method expects the following environment variables to be set: 
     * <ul>
     * <li>AWS_ACCESS_KEY_ID: access key for the AWS account authorized to access the S3 bucket</li>
     * <li>AWS_SECRET_KEY: secret key for this account</li>
     * <li>PARAM1: the name of the bucket where the config file is stored</li>
     * <li>PARAM2: the key to the config properties file</li>
     * </ul>
     * 
     * @param properties the properties object into which to load the config
     */
    private void tryToLoadFromS3(Properties properties) {
        String awsAccessKeyId = System.getProperty("AWS_ACCESS_KEY_ID");
        String awsSecretAccessKey = System.getProperty("AWS_SECRET_KEY");

        if(awsAccessKeyId == null || awsSecretAccessKey == null) {
            logger.info("AWS Credentials not provided, not attempting to load config from S3");
            return;
        }

        String bucket = System.getProperty("PARAM1");
        if(bucket == null) {
            logger.error("AWS Credentials provided, but PARAM1 does not contain the bucket name in which the configuration file is stored");
            return;
        }
        String key = System.getProperty("PARAM2");
        if(key == null) {
        	logger.error("AWS Credentials provided, but PARAM2 does not contain the configuration file's key");
        }
        
        AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey));
        try {
            properties.load(client.getObject(bucket, key).getObjectContent());
        } catch (IOException e) {
            logger.error("Exception reading configuration from S3: " + e.getMessage(), e);
        }
    }

    private File webInfDirectory(ServletContext context) {
        return new File(context.getRealPath("WEB-INF") + File.separator + "sigmah.properties");
    }

    private File tomcatConfigurationDirectory() {
        return  new File(System.getenv("CATALINA_BASE") + File.separator +
                "conf" + File.separator + "sigmah.properties");
    }
}
