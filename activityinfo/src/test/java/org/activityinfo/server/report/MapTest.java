package org.activityinfo.server.report;

import org.activityinfo.server.dao.BaseMapDAOImpl;
import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.MapGenerator;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTMapRenderer;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.GsMapLayer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.Report;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class MapTest {


    @Test
    public void testMap() throws Exception {

        ReportParser parser = new ReportParser();
        InputStream in = ParseTest.class.getResourceAsStream("/report-def/map.xml");

        parser.parse( new InputSource( in ));

        Report report = parser.getReport();
        MapElement mapElement = (MapElement) report.getElements().get(0);
        GsMapLayer gradLayer = (GsMapLayer)mapElement.getLayers().get(0);
                                                           
        Assert.assertEquals(DimensionType.Indicator, gradLayer.getColorDimensions().get(0).getType());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        PivotDAO pivotDAO = new PivotDAOHibernateJdbc(em);
        SiteTableDAO siteDAO = new SiteTableDAOHibernate(em);

        MapGenerator generator = new MapGenerator(pivotDAO, siteDAO, new BaseMapDAOImpl());

        User user = new User();
        user.setLocale("fr");


        generator.generate(user, mapElement, null, null);

        Assert.assertNotNull("content", mapElement.getContent());
                                                            
        ImageMapRenderer renderer = new ImageMapRenderer("war/mapicons");

        File file = new File("target/report-test/map.jpg");
        file.mkdirs();
        file.delete();

        renderer.renderToFile(file, mapElement);

        
        Assert.assertTrue(file.exists());

        renderer = new PPTMapRenderer("war/mapicons");

        file = new File("target/report-test/map.ppt");
        file.mkdirs();
        file.delete();

        renderer.renderToFile(file, mapElement);

        Assert.assertTrue(file.exists());

    }

}
