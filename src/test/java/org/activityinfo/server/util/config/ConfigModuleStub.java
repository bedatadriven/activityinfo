package org.activityinfo.server.util.config;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ConfigModuleStub extends AbstractModule {

    @Override
    protected void configure() {
        
    }
    
    @Provides
    public DeploymentConfiguration provideDeploymentConfig() {
        Properties properties = new Properties();
        return new DeploymentConfiguration(properties);
    }

}
