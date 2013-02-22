

package org.activityinfo.client.page;

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

import org.activityinfo.client.page.app.Section;

/**
 * Describes the state of a Page.
 *
 * After the PageId, the PageState is the second component of the
 * application's history management.
 *
 * Not all changes to a Page's state need to be tracked, but large changes
 * to appearance, such as grid paging, are logical to integrate into the
 * browser's history stack.
 *
 *
 *
 * @author Alex Bertram
 */
public interface PageState {

    /**
     * @return a representation of this PageState as a history token
     */
    String serializeAsHistoryToken();

    /**
     *  @return Returns id to which this PageState belongs
     */
	PageId getPageId();


    /**
     * PageIds of PageFrame
     *
     * @return
     */
	List<PageId> getEnclosingFrames();
	
	
	/**
	 * 
	 * @return the section to which this place belongs
	 */
	Section getSection();

}
