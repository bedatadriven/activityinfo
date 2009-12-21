/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server;

import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.Singleton;
import com.google.inject.Provides;
import com.google.inject.Injector;
import org.activityinfo.server.servlet.RootServlet;
import org.activityinfo.server.servlet.ExportServlet;
import org.activityinfo.server.servlet.MapIconServlet;
import org.activityinfo.server.servlet.ReportServlet;
import org.activityinfo.server.filter.CacheFilter;
import org.activityinfo.server.endpoint.gwtrpc.CommandServlet;
import org.activityinfo.server.endpoint.gwtrpc.DownloadServlet;
import org.activityinfo.server.endpoint.wfs.WfsServlet;
import org.activityinfo.server.endpoint.kml.KmlLinkServlet;
import org.activityinfo.server.endpoint.kml.KmlDataServlet;
import org.activityinfo.server.report.generator.MapIconPath;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.Job;
import org.quartz.SchedulerException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

import java.io.File;
import java.io.IOException;

/**
 * @author Alex Bertram
 */
public class ActivityInfoServletModule extends ServletModule {

    @Override
    protected void configureServlets() {

        serve("/").with(RootServlet.class);
        serve("/auth").with(RootServlet.class);

        filter("/Application/*").through(CacheFilter.class);
        serve("/Application/cmd").with(CommandServlet.class);
        serve("/Application/download").with(DownloadServlet.class);
        serve("/Application/export*").with(ExportServlet.class);

        serve("/wfs").with(WfsServlet.class);
        serve("/wfs*").with(WfsServlet.class);
        serve("/icon").with(MapIconServlet.class);
        serve("/report").with(ReportServlet.class);

        bind(String.class)
                .annotatedWith(MapIconPath.class)
                .toProvider(StartupListener.MapIconPathProvider.class)
                .in(Singleton.class);

    }


    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("activityInfo");
    }

    @Provides
    @RequestScoped
    public EntityManager provideEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }

    @Provides @Singleton
    JobFactory provideJobFactory(final Injector injector) {
        return new JobFactory() {
            public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
                return (Job) injector.getInstance(bundle.getJobDetail().getJobClass());
            }
        };
    }


    @Provides @Singleton
    Configuration provideFreemarkerConfiguration(ServletContext context) {
        Configuration cfg = new Configuration();
        // Specify the data source where the template files come from.
        // Here I set a file directory for it:
        try {
            cfg.setDirectoryForTemplateLoading(
                    new File(context.getRealPath("/ftl")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        cfg.setDefaultEncoding("UTF-8");

        // Specify how templates will see the data-model. This is an advanced topic...
        // but just use this:
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        return cfg;
    }
}
