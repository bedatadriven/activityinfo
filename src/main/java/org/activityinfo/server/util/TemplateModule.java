/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.util;

import java.util.ResourceBundle;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/**
 * Provides Dependency Injection of the FreeMarker Template Configuration object.
 */
public class TemplateModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public Configuration provideConfiguration() throws TemplateModelException {
    	
    	
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(TemplateModule.class, "/template");
        config.setDefaultEncoding("UTF-8");
        return config;
    }
}
