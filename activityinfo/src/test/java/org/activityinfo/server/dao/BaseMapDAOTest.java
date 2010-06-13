package org.activityinfo.server.dao;

import org.activityinfo.shared.map.LocalBaseMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

public class BaseMapDAOTest {

    @Test
    public void testBaseMapDAO() {

        // check to see if the tiles folder exists; if not, skip this test
        File tileRoot = new File("c:/tiles");
        if (!tileRoot.isDirectory()) {
            return;
        }

        BaseMapDAO dao = new BaseMapDAOImpl(new Properties());
        LocalBaseMap gray = (LocalBaseMap) dao.getBaseMap("zs.gray.cd");

        Assert.assertEquals("Carte Gris avec Zones de Sante", gray.getName());
        Assert.assertEquals("RGC", gray.getCopyright());
        Assert.assertEquals("version", 1, gray.getVersion());
        Assert.assertEquals("min zoom", 6, gray.getMinZoom());
        Assert.assertEquals("max zoom", 9, gray.getMaxZoom());

        File tile = new File(gray.getLocalTilePath(6, 34, 34));
        Assert.assertTrue(tile.exists());
    }
}
