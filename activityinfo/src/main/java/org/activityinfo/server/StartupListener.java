package org.activityinfo.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.activityinfo.server.auth.AuthenticationModule;
import org.activityinfo.server.bootstrap.BootstrapModule;
import org.activityinfo.server.dao.hibernate.HibernateModule;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.endpoint.kml.KmlModule;
import org.activityinfo.server.endpoint.wfs.WfsModule;
import org.activityinfo.server.mail.MailModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


/**
 * A Servlet context listener that initializes the Dependency Injection Framework (Guice)
 * upon startup.
 *
 * @author Alex Bertram
 */
public class StartupListener extends GuiceServletContextListener {

    private static Logger logger = Logger.getLogger(StartupListener.class);
   
    private ServletContext context;
    public static final String INJECTOR_NAME = StartupListener.class.getName();



    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("ActivityInfo servlet context is initializing");

        context = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
    }


    @Override
    protected Injector getInjector() {
        logger.trace("Injector is being created");

        Injector injector = Guice.createInjector(
                new ConfigModule(), new LoggingModule(),
                new TemplateModule(), new BeanMappingModule(), new MailModule(),
                new HibernateModule(),
                new AuthenticationModule(),
                new ReportModule(),
                new BootstrapModule(),
                new GwtRpcModule(),
                new WfsModule(),
                new KmlModule());

//        ScheduleInitializer si = injector.getInstance(ScheduleInitializer.class);
//        si.init();

        context.setAttribute(INJECTOR_NAME, injector);

        return injector;
    }

}
