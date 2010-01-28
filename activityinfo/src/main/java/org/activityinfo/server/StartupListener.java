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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


/**
 * A Servlet context listener that initializes the Dependency Injection Framework (Guice)
 * upon startup.
 *
 * @author Alex Bertram
 */
public class StartupListener extends GuiceServletContextListener {

    private ServletContext context;
    public static final String INJECTOR_NAME = StartupListener.class.getName();


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
                new TemplateModule(), new BeanMappingModule(), new MailModule(),
                new HibernateModule(),
                new AuthenticationModule(),
                new ReportModule(),
                new BootstrapModule(),
                new GwtRpcModule(),
                new WfsModule(),
                new MailModule(),
                new KmlModule());

//        ScheduleInitializer si = injector.getInstance(ScheduleInitializer.class);
//        si.init();

        context.setAttribute(INJECTOR_NAME, injector);

        return injector;
    }

}
