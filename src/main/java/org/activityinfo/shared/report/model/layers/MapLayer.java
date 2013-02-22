package org.activityinfo.shared.report.model.layers;

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

import java.io.Serializable;
import java.util.List;

import org.activityinfo.shared.command.Filter;

/**
 * A layer representing one or more indicators on a MapElement
 */
public interface MapLayer extends Serializable {

    boolean isVisible();

    void setVisible(boolean isVisible);

    /**
     * Gets the list of indicators to map. The value at each site used for
     * scaling is equal to the sum of the values of the indicators in this list,
     * or 1.0 if no indicators are specified.
     */
    List<Integer> getIndicatorIds();

    void addIndicatorId(int id);

    boolean supportsMultipleIndicators();

    boolean hasMultipleIndicators();

    String getName();

    void setName(String name);

    /*
     * Function to determine non-typesafe name of the class for gxt template
     * usage
     */
    String getTypeName();

    Filter getFilter();

    void setFilter(Filter filter);
}