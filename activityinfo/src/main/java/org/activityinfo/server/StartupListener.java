package org.activityinfo.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceServletContextListener;
import org.activityinfo.server.bootstrap.BootstrapModule;
import org.activityinfo.server.dao.hibernate.ServletDataModule;
import org.activityinfo.server.endpoint.kml.KmlModule;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


/**
 * A Servlet context listener that initializes the Dependency Injection Framework (Guice)
 * upon startup.
 * <p/>
 * Most of the bindings are defined in {@link org.activityinfo.server.ActivityInfoModule}, except
 * those that are servlet-specific.
 * <p/>
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
                new TemplateModule(), new BeanMappingModule(),
                new ServletDataModule(),
                new BootstrapModule(),
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
