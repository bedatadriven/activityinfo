package org.activityinfo.server.report.renderer;

import org.activityinfo.server.dao.BaseMapDAOImpl;
import org.activityinfo.server.dao.hibernate.PivotDAOHibernateJdbc;
import org.activityinfo.server.dao.hibernate.SiteTableDAOHibernate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.MapGenerator;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTMapRenderer;
import org.activityinfo.shared.report.model.*;
import org.junit.Test;
import org.junit.Assert;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
/*
 * @author Alex Bertram
 */

public class MapIntegrationTest {

    @Test
    public void testImage() throws IOException {

        GsMapLayer gsLayer = new GsMapLayer();
        gsLayer.addIndicator(11);
        gsLayer.addIndicator(5);
        gsLayer.setMaxRadius(10);
        gsLayer.setMinRadius(5);

        Dimension dimension = new Dimension(DimensionType.Indicator);
        dimension.setProperties(11, CategoryProperties.Color(0, 0, 255));
        dimension.setProperties(5, CategoryProperties.Color(255, 0, 0));
        gsLayer.getColorDimensions().add(dimension);

        IconMapLayer iconLayer = new IconMapLayer();
        iconLayer.setClustered(false);
        iconLayer.setIcon("nfi");
        iconLayer.addActivityId(33);

        MapElement element = new MapElement();
        element.setBaseMapId("zs.gray.cd");
        element.setWidth(640);
        element.setHeight(480);
        element.addLayer(gsLayer);
        element.addLayer(iconLayer);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("activityInfo");
        EntityManager em = emf.createEntityManager();

        PivotDAOHibernateJdbc pivotDAO = new PivotDAOHibernateJdbc(em);
        SiteTableDAOHibernate siteDAO = new SiteTableDAOHibernate(em);

        MapGenerator gtor = new MapGenerator(pivotDAO, siteDAO, new BaseMapDAOImpl());

        gtor.generate(new User(), element, null, new HashMap<String, Object>());

        Assert.assertNotNull("content",element.getContent());
        Assert.assertTrue(element.getContent().getMarkers().size() > 0);


        File outputFolder = new File("target/report-tests");
        outputFolder.mkdirs();

        File outputFile = new File(outputFolder.getAbsolutePath() + "/indicator-map.png");

        if(outputFile.exists())
            outputFile.delete();

        ImageMapRenderer rdr = new ImageMapRenderer("war/mapicons");
        rdr.renderToFile(outputFile, element);

        Assert.assertTrue(outputFile.exists());

        File outputPptFile = new File(outputFolder.getAbsolutePath() + "/indicator-map.ppt");

        PPTMapRenderer pptRdr = new PPTMapRenderer("war/mapicons");
        pptRdr.renderToFile(outputPptFile, element);
    }
}
