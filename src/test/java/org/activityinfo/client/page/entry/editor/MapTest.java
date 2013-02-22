
package org.activityinfo.client.page.entry.editor;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */



/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapTest {

    private static final double DELTA = 0.001;
//
//    @Test
//    public void testMapInitialView() {
//
//        // Collaborator: view
//        MockMapView map = new MockMapView();
//
//        // Class under test
//        MapPresenter presenter = new MapPresenter(map);
//
//        // VERIFY that the initial view is set to the admin bounds,
//        // and that, in the absence of X/Y data, the marker is set to
//        // the center of these Bounds
//
//        Extents bounds = new Extents(0, 0, 300, 300);
//
//        presenter.setSite(new SiteDTO(), null, bounds);
//
//        Assert.assertEquals(bounds, map.getViewBounds());	
//        Assert.assertEquals(bounds.getCenterX(), map.getX(), DELTA);
//        Assert.assertEquals(bounds.getCenterY(), map.getY(), DELTA);
//    }
//
//
//    @Test
//    public void testMarkerMove() {
//        // Collaborator: view
//        MockMapView map = new MockMapView();
//
//        // Class under test
//        MapPresenter presenter = new MapPresenter(map);
//
//        // VERIFY that marker movement updates the coordinates
//
//        Extents bounds = new Extents(0, 0, 300, 200);
//        SiteDTO site = new SiteDTO();
//        site.setX(25.0);
//        site.setY(30.0);
//        presenter.setSite(site, null, bounds);
//
//        presenter.onMarkedMoved(new MarkerMovedEvent(50.0, 65.0));
//
//        Assert.assertEquals("y", 50.0, map.getY(), DELTA);
//        Assert.assertEquals("x", 65.0, map.getX(), DELTA);
//
//        // VERIFY that attempting to move the marker out of the
//        // bounds results in clamping
//
//        presenter.onMarkedMoved(new MarkerMovedEvent(900, 800));
//
//        // The marker should not be placed beyond the edit bounds
//        Assert.assertEquals("marker x clamped", 300.0, map.getValue().getLng(), DELTA);
//        Assert.assertEquals("marker y clamped", 200.0, map.getValue().getLat(), DELTA);
//        
//        // The view should not be able to pan out of the viewing bounds
//        Assert.assertEquals("coord x clamped", 300.0, map.getX(), DELTA);
//        Assert.assertEquals("coord y clamped", 200.0, map.getY(), DELTA);
//    }
}
