package org.activityinfo.server.report.renderer.ppt;

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.activityinfo.server.report.renderer.image.TileHandler;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.ShapeGroup;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import com.google.code.appengine.awt.Rectangle;
import com.google.common.io.Resources;

public class PPTTileHandler implements TileHandler {

    private SlideShow ppt;
    private Slide slide;
    private ShapeGroup shapeGroup;

    public PPTTileHandler(SlideShow ppt, Slide slide) {
        this.ppt = ppt;
        this.slide = slide;
        this.shapeGroup = new ShapeGroup();
        this.slide.addShape(shapeGroup);
    }

    @Override
    public void addTile(String tileUrl, int x, int y, int width, int height) {
        try {
            byte[] imageBytes = Resources.toByteArray(new URL(tileUrl));
            int pictureIndex = ppt.addPicture(imageBytes, Picture.PNG);
            Picture basemap = new Picture(pictureIndex);
            basemap.setAnchor(new Rectangle(x, y, width, height));
            basemap.setLineWidth(0);
            shapeGroup.addShape(basemap);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad tile URL", e);
        } catch (IOException e) {
            // ignore missing tiles
        }
    }
}
