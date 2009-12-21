package org.activityinfo.server;

import com.google.inject.*;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.activityinfo.server.endpoint.kml.KmlModule;


/**
 *
 * A Servlet context listener that initializes the Dependency Injection Framework (Guice)
 * upon startup.
 *
 * Most of the bindings are defined in {@link org.activityinfo.server.ActivityInfoModule}, except
 * those that are servlet-specific.
 *
 * See http://code.google.com/p/google-guice/wiki/ServletModule for details
 *
 * @author Alex Bertram
 */
public class StartupListener extends GuiceServletContextListener {

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

        Injector injector = Guice.createInjector(
                new ActivityInfoModule(),
                new ActivityInfoServletModule(),
                new KmlModule());

//        ScheduleInitializer si = injector.getInstance(ScheduleInitializer.class);
//        si.init();

        context.setAttribute(ActivityInfoModule.INJECTOR_NAME, injector);

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
