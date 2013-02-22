

package org.activityinfo.client.page.common.nav;

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

import java.util.List;

import com.extjs.gxt.ui.client.data.DataProxy;

/**
 * Provides the data for the contents of a
 * {@link org.activityinfo.client.page.common.nav.NavigationPanel}
 *
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface Navigator extends DataProxy<List<Link>> {

    /**
     *
     * @return The text to display in the heading of the navigation panel
     */
    public String getHeading();

    /**
     *
     * @param parent
     * @return True if the given parent has children
     */
    public boolean hasChildren(Link parent);

    /**
     * @return A unique id used for storing the state of this
     * tree.
     */
    String getStateId();
}
