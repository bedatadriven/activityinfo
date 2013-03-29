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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.AccessControlException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.activityinfo.server.DeploymentEnvironment;
import org.activityinfo.server.util.logging.Trace;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.appengine.api.utils.SystemProperty;
import com.google.common.base.Strings;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

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
        if (DeploymentEnvironment.isAppEngine()) {
            bind(AppengineConfigResource.class);
            filter("/admin/config").through(GuiceContainer.class);
        }
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
        tryToLoadFromS3(properties);
        if (DeploymentEnvironment.isAppEngine()) {
            tryToLoadFromAppEngineDatastore(properties);
        }

        // specified at server start up with
        // -Dactivityinfo.config=/path/to/conf.properties
        if (!Strings.isNullOrEmpty(System.getProperty("activityinfo.config"))) {
            tryToLoadFrom(properties,
                new File(System.getProperty("activityinfo.config")));
        }

        return new DeploymentConfiguration(properties);
    }

    @Provides
    public AWSCredentials provideAWSCredentials(DeploymentConfiguration config) {
        return config.getAWSCredentials();
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

    /**
     * 
     * Tries to load a configuration file from an external object stored in
     * Amazon S3.
     * 
     * This is very useful in automating production and continuous deployment
     * scenarios because it allows us to keep the config and the app artifact
     * seperate.
     * 
     * This method expects the following environment variables to be set:
     * <ul>
     * <li>AWS_ACCESS_KEY_ID: access key for the AWS account authorized to
     * access the S3 bucket</li>
     * <li>AWS_SECRET_KEY: secret key for this account</li>
     * <li>PARAM1: the name of the bucket where the config file is stored</li>
     * <li>PARAM2: the key to the config properties file</li>
     * </ul>
     * 
     * @param properties
     *            the properties object into which to load the config
     */
    private void tryToLoadFromS3(Properties properties) {
        if (!BeanstalkEnvironment.credentialsArePresent()) {
            logger
                .info("AWS Credentials not provided, not attempting to load config from S3");
            return;
        }

        String awsAccessKeyId = BeanstalkEnvironment.getAccessKeyId();
        String awsSecretAccessKey = BeanstalkEnvironment.getSecretKey();
        String bucket = BeanstalkEnvironment.getConfigurationPropertiesBucket();
        if (Strings.isNullOrEmpty(bucket)) {
            logger
                .log(
                    Level.SEVERE,
                    "AWS Credentials provided, but PARAM1 does not contain the bucket name in which the configuration file is stored");
            return;
        }
        String key = BeanstalkEnvironment.getConfigurationPropertiesKey();
        if (Strings.isNullOrEmpty(key)) {
            logger
                .log(
                    Level.SEVERE,
                    "AWS Credentials provided, but PARAM2 does not contain the configuration file's key");
        }

        AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
            awsAccessKeyId, awsSecretAccessKey));
        try {
            properties.load(client.getObject(bucket, key).getObjectContent());
            logger.info("Loaded configuration from S3 " + bucket + "/" + key);
        } catch (IOException e) {
            logger
                .log(Level.SEVERE, "Exception reading configuration from S3: "
                    + e.getMessage(), e);
        }
    }

    private void tryToLoadFromAppEngineDatastore(Properties properties) {
        try {
            String config = AppEngineConfig.getPropertyFile();
            if (!Strings.isNullOrEmpty(config)) {
                logger.info("Read config from datastore: \n" + config);
                properties.load(new StringReader(config));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                "Exception reading configuration from AppEngine Datastore", e);
        }
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
