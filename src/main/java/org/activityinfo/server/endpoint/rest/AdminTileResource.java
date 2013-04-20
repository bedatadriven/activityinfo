package org.activityinfo.server.endpoint.rest;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.activityinfo.shared.util.mapping.Extents;
import org.activityinfo.shared.util.mapping.TileMath;

import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.color.ColorSpace;
import com.google.code.appengine.awt.image.BufferedImage;
import com.sun.jersey.api.core.InjectParam;

public class AdminTileResource {

    @GET
    @Path("/{z}/{x}/{y}.png")
    public String getTile(@InjectParam EntityManager em,
        @PathParam("z") int zoomLevel, @PathParam("x") int x, @PathParam("y") int y) {

        Extents extents = TileMath.tileBounds(zoomLevel, x, y);
        
        BufferedImage image = new BufferedImage(TileMath.TILE_SIZE, TileMath.TILE_SIZE,
            ColorSpace.TYPE_RGB);
        Graphics2D g2d = image.createGraphics();
        return "";
        
    }
    
    
}
