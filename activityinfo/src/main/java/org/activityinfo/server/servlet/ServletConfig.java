package org.activityinfo.server.servlet;

import com.google.inject.*;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import org.activityinfo.server.ActivityInfoModule;
import org.activityinfo.server.schedule.ReportMailerJob;
import org.activityinfo.server.schedule.ScheduleInitializer;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.ServletImageStorageProvider;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;
import org.activityinfo.server.servlet.kml.KmlDataServlet;
import org.activityinfo.server.servlet.kml.KmlLinkServlet;
import org.activityinfo.server.servlet.wfs.WfsServlet;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

import java.io.File;
import java.io.IOException;/*


 * @author Alex Bertram
 */

public class ServletConfig extends GuiceServletContextListener {

    private ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        context = servletContextEvent.getServletContext();

        context.log("ServletConfig.contextIntialized called.");

        super.contextInitialized(servletContextEvent);
    }


    @Override
    protected Injector getInjector() {

        context.log("Injector.getInjector");

        Injector injector = Guice.createInjector(new ActivityInfoModule(), new ServletModule() {

            @Override
            protected void configureServlets() {

                context.log("configureServlets");

                serve("/Application/cmd").with(RemoteCommandServlet.class);
                serve("/").with(RootServlet.class);
                serve("/auth").with(RootServlet.class);
                serve("/wfs").with(WfsServlet.class);
                serve("/wfs*").with(WfsServlet.class);
                serve("/kml").with(KmlLinkServlet.class);
                serve("/kml/data").with(KmlDataServlet.class);
                serve("/icon").with(MapIconServlet.class);
                serve("/download").with(DownloadServlet.class);

                bind(String.class)
                        .annotatedWith(MapIconPath.class)
                        .toProvider(MapIconPathProvider.class)
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
        });

//        ScheduleInitializer si = injector.getInstance(ScheduleInitializer.class);
//        si.init();


        return injector;
    }

    protected static class MapIconPathProvider implements Provider<String> {

        private String path;

        @Inject
        public MapIconPathProvider(ServletContext context) {
            path = context.getRealPath("/mapicons");
        }

        public String get() {
            return path;
        }
    }
}
