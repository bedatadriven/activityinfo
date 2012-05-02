/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.activityinfo.login.server.LoginModule;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.sigmah.server.authentication.AuthenticationModule;
import org.sigmah.server.bootstrap.BootstrapModule;
import org.sigmah.server.database.ServerDatabaseModule;
import org.sigmah.server.database.hibernate.dao.HibernateModule;
import org.sigmah.server.endpoint.content.ContentModule;
import org.sigmah.server.endpoint.crx.CrxModule;
import org.sigmah.server.endpoint.export.ExportModule;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.endpoint.jsonrpc.JsonRpcModule;
import org.sigmah.server.endpoint.kml.KmlModule;
import org.sigmah.server.i18n.LocaleModule;
import org.sigmah.server.mail.MailModule;
import org.sigmah.server.report.ReportModule;
import org.sigmah.server.schedule.QuartzModule;
import org.sigmah.server.schedule.ReportMailerJob;
import org.sigmah.server.util.TemplateModule;
import org.sigmah.server.util.beanMapping.BeanMappingModule;
import org.sigmah.server.util.config.ConfigModule;
import org.sigmah.server.util.logging.LoggingModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;


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
        //logger.trace("Injector is being created");

        Injector injector = Guice.createInjector(
                new ConfigModule(), new LoggingModule(),
                new TemplateModule(), new BeanMappingModule(), new MailModule(),
                new HibernateModule(), 
                new QuartzModule(),
                new ContentModule(),
                new ServerDatabaseModule(),
                new AuthenticationModule(),
                new ReportModule(),
                new BootstrapModule(),
                new GwtRpcModule(),
                new ExportModule(),
                new JsonRpcModule(),
                new KmlModule(),
                new CrxModule(),
                new LocaleModule(),
                new LoginModule());

        context.setAttribute(INJECTOR_NAME, injector);   
        return injector;
    }
	
}