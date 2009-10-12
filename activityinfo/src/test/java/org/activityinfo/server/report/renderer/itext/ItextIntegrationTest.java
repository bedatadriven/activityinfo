package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.dao.BaseMapDAO;
import org.activityinfo.server.dao.BaseMapDAOImpl;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.dao.jpa.SchemaDAOJPA;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.server.report.generator.*;
import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.Report;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class ItextIntegrationTest  {


    @Test
    public void testPDF() throws Exception {

        // PARSE
		ReportParser parser = new ReportParser();
		InputStream in = ItextIntegrationTest.class.getResourceAsStream("/report-def/rrm.xml");
		parser.parse( new InputSource( in ));
        Report report = parser.getReport();

        // GENERATE

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        PivotDAO pivotDAO = new PivotDAOHibernateJdbc(em);
        SchemaDAOJPA schemaDAO = new SchemaDAOJPA(em);
        SiteTableDAO siteDAO = new SiteTableDAOHibernate(em);
        BaseMapDAO baseMapDAO = new BaseMapDAOImpl();

        PivotTableGenerator ptGtor = new PivotTableGenerator(pivotDAO);
        PivotChartGenerator pcGtor = new PivotChartGenerator(pivotDAO, schemaDAO);
        TableGenerator tGtor = new TableGenerator(pivotDAO, siteDAO, schemaDAO);
        MapGenerator mGtor = new MapGenerator(pivotDAO, siteDAO, baseMapDAO );

        ReportGenerator gtor = new ReportGenerator(pivotDAO, ptGtor, pcGtor, tGtor, mGtor );

        User user = new User("a@gmail.com", "Alex", "fr");
        gtor.generate(user, report, new Filter(), null);

        // RENDER

        File pdf = new File("target/report-tests/ItextIntegrationTest.pdf");
        FileOutputStream fos = new FileOutputStream(pdf);

        ItextPivotTableRenderer pivotTableRenderer = new ItextPivotTableRenderer();
        ItextChartRenderer pivotChartRenderer = new ItextChartRenderer(new ChartRendererJC());
        ItextMapRenderer mapRenderer = new ItextMapRenderer("war/mapicons");

        PdfReportRenderer pdfRenderer = new PdfReportRenderer(pivotTableRenderer, pivotChartRenderer,
                mapRenderer);

        pdfRenderer.render(report, fos);
        fos.close();

        File rtf = new File("target/report-tests/ItextIntegrationTest.rtf");
        fos = new FileOutputStream(rtf);

        RtfReportRenderer rtfReportRenderer = new RtfReportRenderer(pivotTableRenderer, pivotChartRenderer,
                mapRenderer);
        rtfReportRenderer.render(report, fos);
        fos.close();

    }


}
