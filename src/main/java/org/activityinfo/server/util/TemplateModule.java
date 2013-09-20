package org.activityinfo.server.util;

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

import org.activityinfo.server.database.hibernate.entity.Domain;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Provides Dependency Injection of the FreeMarker Template Configuration
 * object.
 */
public class TemplateModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public Configuration provideConfiguration(Provider<Domain> domainProvider) throws TemplateModelException {

        Configuration config = new Configuration();
        config.setClassForTemplateLoading(TemplateModule.class, "/template");
        config.setDefaultEncoding("UTF-8");
        config.setSharedVariable("domain", new InjectedTemplateModel<Domain>(domainProvider));
        return config;
    }
    
    private static class InjectedTemplateModel<T> implements TemplateHashModel {
    	private Provider<T> provider;
    
    	public InjectedTemplateModel(Provider<T> provider) {
			super();
			this.provider = provider;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {
			BeanModel model = new BeanModel(provider.get(), new BeansWrapper());
			return model.get(key);
		}

		@Override
		public boolean isEmpty() throws TemplateModelException {
			return false;
		}
    }
}
