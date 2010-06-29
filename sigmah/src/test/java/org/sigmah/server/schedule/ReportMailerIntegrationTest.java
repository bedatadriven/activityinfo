/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.schedule;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @author Alex Bertram
 */
public class ReportMailerIntegrationTest {


    @Test
    public void test() throws IOException, SAXException, JAXBException {
//
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
//        EntityManager em = emf.createEntityManager();
//
//        PivotDAO pivotDAO = new PivotDAOHibernateJdbc(em);
//        SchemaDAO schemaDAO = new SchemaDAOJPA(em);
//        SiteTableDAO siteTableDAO = new SiteTableDAOHibernate(em);
//        BaseMapDAO baseMapDAO = new BaseMapDAOImpl();
//
//        PivotTableGenerator ptgen = new PivotTableGenerator(pivotDAO);
//        PivotChartGenerator pcgen = new PivotChartGenerator(pivotDAO, schemaDAO);
//        MapGenerator mgen = new MapGenerator(pivotDAO, siteTableDAO, baseMapDAO);
//        TableGenerator tgen = new TableGenerator(pivotDAO, siteTableDAO, schemaDAO, mgen);
//
//        ReportGenerator rgen = new ReportGenerator(pivotDAO, ptgen, pcgen, tgen, mgen);
//
//        RtfReportRenderer rtfren = new RtfReportRenderer(new ItextPivotTableRenderer(),
//                new ItextChartRenderer(new ChartRendererJC()), new ItextMapRenderer("war/mapicons"),
//                new ItextTableRenderer(new ItextMapRenderer("war/mapicons")));
//
//        ReportMailerJob job = new ReportMailerJob(em, rgen, rtfren, new MailerImpl() );
//
//
//        User user = new User("akbertram@gmail.com", "Alex", "fr");
//
//        Calendar cal = Calendar.getInstance();
//
//        List<ReportDefinition> templates = em.createQuery("select t from ReportDefinition t").getResultList();
//
//        for(ReportDefinition template : templates) {
//
//            Set<ReportSubscription> subs = new HashSet<ReportSubscription>();
//
//            Report report = ReportParserJaxb.parseXml(template.getXml());
//
//            ReportSubscription sub = new ReportSubscription();
//            sub.setUser(user);
//            sub.setTemplate(template);
//            subs.add(sub);
//
//
//            job.execute(new Date(), report, subs);
//        }


    }

}
