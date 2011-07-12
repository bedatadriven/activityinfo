/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.sigmah.server.util.logging.Trace;

import javax.servlet.ServletContext;
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

    private void tryToLoadFromS3(Properties properties) {
        String awsAccessKeyId = System.getProperty("AWS_ACCESS_KEY_ID");
        String awsSecretAccessKey = System.getProperty("AWS_SECRET_KEY");

        if(awsAccessKeyId == null || awsSecretAccessKey == null) {
            logger.info("AWS Credentials not provided, not attempting to load config from S3");
            return;
        }

        String config = System.getProperty("PARAM1");
        if(config == null) {
            logger.error("AWS Credentials provided, but PARAM1 does not contain bucket/key of configuration file");
            return;
        }
        int slash = config.indexOf('/');
        if(slash == -1) {
            logger.error("AWS Credentials provided, but PARAM1 not in expected bucket/key format");
        }
        String bucket = config.substring(0, slash);
        String key = config.substring(slash+1);

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
