package org.activityinfo.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.hibernate.SchemaDAOJPA;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.MailSenderImpl;
import org.activityinfo.server.report.renderer.html.HtmlChartRenderer;
import org.activityinfo.server.report.renderer.html.HtmlChartRendererJC;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines application-wide bindings for the Guice Dependency Injection Framework.
 */
@Deprecated
public class ActivityInfoModule extends AbstractModule {

    public static final String INJECTOR_NAME = ActivityInfoModule.class.getName();

    @Override
    protected void configure() {

        /* DAO Objects */
        bind(SchemaDAO.class).to(SchemaDAOJPA.class);

        /* External Services */
        bind(MailSender.class).to(MailSenderImpl.class);

        /* Renderers */
        bind(HtmlChartRenderer.class).to(HtmlChartRendererJC.class);

    }

    @Provides
    @Singleton
    Mapper provideMapper() {

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
