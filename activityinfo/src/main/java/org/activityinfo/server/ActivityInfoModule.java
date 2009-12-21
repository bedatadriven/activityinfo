package org.activityinfo.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.activityinfo.server.dao.*;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.dao.jpa.*;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.server.mail.MailerImpl;
import org.activityinfo.server.report.renderer.html.HtmlChartRenderer;
import org.activityinfo.server.report.renderer.html.HtmlChartRendererJC;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.server.service.PasswordGenerator;
import org.activityinfo.server.service.impl.CongoPasswordGenerator;
import org.activityinfo.server.service.impl.DbAuthenticator;
import org.activityinfo.server.service.impl.NullMailer;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines application-wide bindings for the Guice Dependency Injection Framework.
 */
public class ActivityInfoModule extends AbstractModule {

    public static final String INJECTOR_NAME = ActivityInfoModule.class.getName();

    @Override
    protected void configure() {

        /* DAO Objects */
        bind(AdminDAO.class).to(AdminDAOJPA.class);
        bind(AuthDAO.class).to(AuthDAOJPA.class);
        bind(ReportDAO.class).to(ReportDAOJPA.class);
        bind(SchemaDAO.class).to(SchemaDAOJPA.class);
        bind(SiteDAO.class).to(SiteDAOJPA.class);
        bind(SiteTableDAO.class).to(SiteTableDAOHibernate.class);
        bind(PivotDAO.class).to(PivotDAOHibernateJdbc.class);
        bind(BaseMapDAO.class).to(BaseMapDAOImpl.class);

        /* External Services */
        bind(Mailer.class).to(MailerImpl.class);
        bind(PasswordGenerator.class).to(CongoPasswordGenerator.class);
        bind(Authenticator.class).to(DbAuthenticator.class);

        /* Renderers */
        bind(HtmlChartRenderer.class).to(HtmlChartRendererJC.class);

    }

    @Provides @Singleton Mapper provideMapper() {

        List<String> mappingFiles = new ArrayList<String>();
        mappingFiles.add("dozer-admin-mapping.xml");
        mappingFiles.add("dozer-schema-mapping.xml");

        return new DozerBeanMapper(mappingFiles);

    }

    public static Mapper getMapper() {
        ActivityInfoModule mod = new ActivityInfoModule();
        return mod.provideMapper();
    }

}
