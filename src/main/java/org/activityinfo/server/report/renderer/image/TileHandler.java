package org.activityinfo.server.report.renderer.image;

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

public interface TileHandler {

    /**
     * Renders a tile to the map canvas. The coordinate space has its origin in
     * the UPPER-LEFT hand corner of the canvas. (Y increasing downwards)
     * 
     * @param tileUrl
     *            the url of the tile
     * @param x
     *            the horizontal position to place the tile image (left)
     * @param y
     *            the vertical position to place the tile image (top)
     * @param width
     * @param height
     */
    void addTile(String tileUrl, int x, int y, int width, int height);

}
