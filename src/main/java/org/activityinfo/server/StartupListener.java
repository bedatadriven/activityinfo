/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server;

import java.util.Random;

import javax.servlet.ServletContextEvent;

import org.activityinfo.login.server.LoginModule;
import org.activityinfo.server.attachment.AttachmentModule;
import org.activityinfo.server.authentication.AuthenticationModule;
import org.activityinfo.server.bootstrap.BootstrapModule;
import org.activityinfo.server.database.ServerDatabaseModule;
import org.activityinfo.server.database.hibernate.HibernateModule;
import org.activityinfo.server.endpoint.content.ContentModule;
import org.activityinfo.server.endpoint.export.ExportModule;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.endpoint.healthcheck.HealthCheckModule;
import org.activityinfo.server.endpoint.jsonrpc.JsonRpcModule;
import org.activityinfo.server.endpoint.kml.KmlModule;
import org.activityinfo.server.geo.GeometryModule;
import org.activityinfo.server.i18n.LocaleModule;
import org.activityinfo.server.mail.MailModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.server.schedule.QuartzModule;
import org.activityinfo.server.util.TemplateModule;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.server.util.config.ConfigModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.server.util.monitoring.MonitoringModule;
import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;


/**
 * A Servlet context listener that initializes the Dependency Injection Framework (Guice)
 * upon startup.
 *
 */
public class StartupListener extends GuiceServletContextListener {

    private static Logger logger = Logger.getLogger(StartupListener.class.getName());

	private Random RNG = new Random();


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("ActivityInfo servlet context is initializing");
        super.contextInitialized(servletContextEvent);
    }


	@Override
    protected Injector getInjector() {

        return Guice.createInjector(
                new ConfigModule(), new LoggingModule(),
                new TemplateModule(), new BeanMappingModule(), new MailModule(),
                new ServerDatabaseModule(),
                new HibernateModule(), 
                new ContentModule(),
                new GeometryModule(),
                new QuartzModule(),
                new AuthenticationModule(),
                new AttachmentModule(),
                new ReportModule(),
                new BootstrapModule(),
                new GwtRpcModule(),
                new HealthCheckModule(),
                new ExportModule(),
                new MonitoringModule(),
                new JsonRpcModule(),
                new KmlModule(),
                new LocaleModule(),
                new LoginModule());
    }

}