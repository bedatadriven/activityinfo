package org.activityinfo.server.report.generator.map;

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

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.util.mapping.Extents;

public interface LayerGenerator {

    void query(DispatcherSync dispatcher, Filter effectiveFilter);

    /**
     * Calculates the geographic extents for this layer
     * 
     * @param sites
     * @return A geographic bounding box
     */
    Extents calculateExtents();

    /**
     * Calculates the maximum potential margins (in pixels) outside of the
     * geographic bounds.
     * 
     * @return
     */
    Margins calculateMargins();

    void generate(TiledMap map, MapContent content);
}
