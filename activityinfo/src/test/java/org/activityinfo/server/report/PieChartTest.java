package org.activityinfo.server.report;

import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.server.dao.jpa.SchemaDAOJPA;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.PivotChartGenerator;
import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.shared.report.model.PivotChartElement;
import org.activityinfo.shared.report.model.Report;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class PieChartTest {

    @Test
    public void testPieChart() throws Exception {

        ReportParser parser = new ReportParser();
        InputStream in = ParseTest.class.getResourceAsStream("/report-def/pie-chart.xml");

        parser.parse( new InputSource( in ));

        Report report = parser.getReport();



        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        PivotDAO pivotDAO = new PivotDAOHibernateJdbc(em);
        SchemaDAO schemaDAO = new SchemaDAOJPA(em);

        PivotChartGenerator generator = new PivotChartGenerator(pivotDAO, schemaDAO);

        User user = new User();
        user.setLocale("fr");

        final PivotChartElement chartElement = (PivotChartElement) report.getElements().get(0);

        generator.generate(user, chartElement,
                null, null);

        Assert.assertNotNull("content", chartElement.getContent());

        ChartRendererJC renderer = new ChartRendererJC();
        RenderedImage image = renderer.renderImage(chartElement, false, 600, 300, 72);

        File file = new File("target/report-test/piechart.jpg");
        file.mkdirs();
        file.delete();

        ImageIO.write(image, "JPG", file);

        Assert.assertTrue(file.exists());

    }



}
