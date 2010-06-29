package org.sigmah.client.page.entry.editor;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.client.page.entry.editor.mock.MockMapView;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.dto.SiteDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapTest {

    private static final double DELTA = 0.001;

    @Test
    public void testMapInitialView() {

        // Collaborator: view
        MockMapView map = new MockMapView();

        // Class under test
        MapPresenter presenter = new MapPresenter(map);

        // VERIFY that the initial view is set to the admin bounds,
        // and that, in the absence of X/Y data, the marker is set to
        // the center of these Bounds

        BoundingBoxDTO bounds = new BoundingBoxDTO(0, 0, 300, 300);

        presenter.setSite(new SiteDTO(), null, bounds);

        Assert.assertEquals(bounds, map.getMapView());
        Assert.assertEquals(bounds.getCenterX(), map.markerX, DELTA);
        Assert.assertEquals(bounds.getCenterY(), map.markerY, DELTA);
    }


    @Test
    public void testMarkerMove() {


        // Collaborator: view
        MockMapView map = new MockMapView();

        // Class under test
        MapPresenter presenter = new MapPresenter(map);

        // VERIFY that marker movement updates the coordinates

        BoundingBoxDTO bounds = new BoundingBoxDTO(0, 0, 300, 200);
        SiteDTO site = new SiteDTO();
        site.setX(25.0);
        site.setY(30.0);
        presenter.setSite(site, null, bounds);

        presenter.onMarkerMoved(50.0, 65.0);

        Assert.assertEquals("y", 50.0, map.getY(), DELTA);
        Assert.assertEquals("x", 65.0, map.getX(), DELTA);

        // VERIFY that attempting to move the marker out of the
        // bounds results in clamping

        presenter.onMarkerMoved(900, 800);

        Assert.assertEquals("marker x clamped", 300.0, map.markerX, DELTA);
        Assert.assertEquals("marker y clamped", 200.0, map.markerY, DELTA);
        Assert.assertEquals("coord x clamped", 300.0, map.getX(), DELTA);
        Assert.assertEquals("coord y clamped", 200.0, map.getY(), DELTA);
    }


}
