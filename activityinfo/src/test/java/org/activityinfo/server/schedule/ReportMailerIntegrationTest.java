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

package org.activityinfo.server.schedule;

import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.report.generator.*;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.server.report.renderer.itext.ItextPivotTableRenderer;
import org.activityinfo.server.report.renderer.itext.ItextChartRenderer;
import org.activityinfo.server.report.renderer.itext.ItextMapRenderer;
import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.BaseMapDAO;
import org.activityinfo.server.dao.BaseMapDAOImpl;
import org.activityinfo.server.dao.jpa.SchemaDAOJPA;
import org.activityinfo.server.mail.MailerImpl;
import org.activityinfo.shared.report.model.Report;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import java.util.*;
import java.io.IOException;

/**
 * @author Alex Bertram
 */
public class ReportMailerIntegrationTest {


    @Test
    public void test() throws IOException, SAXException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        PivotDAO pivotDAO = new PivotDAOHibernateJdbc(em);
        SchemaDAO schemaDAO = new SchemaDAOJPA(em);
        SiteTableDAO siteTableDAO = new SiteTableDAOHibernate(em);
        BaseMapDAO baseMapDAO = new BaseMapDAOImpl();

        PivotTableGenerator ptgen = new PivotTableGenerator(pivotDAO);
        PivotChartGenerator pcgen = new PivotChartGenerator(pivotDAO, schemaDAO);
        TableGenerator tgen = new TableGenerator(pivotDAO, siteTableDAO, schemaDAO);
        MapGenerator mgen = new MapGenerator(pivotDAO, siteTableDAO, baseMapDAO);

        ReportGenerator rgen = new ReportGenerator(pivotDAO, ptgen, pcgen, tgen, mgen);

        RtfReportRenderer rtfren = new RtfReportRenderer(new ItextPivotTableRenderer(),
                new ItextChartRenderer(new ChartRendererJC()), new ItextMapRenderer("war/mapicons") );

        ReportMailerJob job = new ReportMailerJob(em, rgen, rtfren, new MailerImpl() );


        User user = new User("akbertram@gmail.com", "Alex", "fr");

        Calendar cal = Calendar.getInstance();

        List<ReportTemplate> templates = em.createQuery("select t from ReportTemplate t").getResultList();

        for(ReportTemplate template : templates) {

            Set<ReportSubscription> subs = new HashSet<ReportSubscription>();

            Report report = ReportParser.parseXml(template.getXml());

            ReportSubscription sub = new ReportSubscription();
            sub.setUser(user);
            sub.setTemplate(template);
            subs.add(sub);


            job.execute(new Date(), report, subs);
        }


    }

}
