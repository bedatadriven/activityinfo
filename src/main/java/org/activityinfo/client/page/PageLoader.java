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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Component responsible for
 */
public interface PageLoader {

    /**
     * Loads the Page for the given pageId, potentially asynchronously.
     * 
     * @param pageId
     *            The ID of the page for which to load the presenter
     * @param pageState
     *            A PageState object describing the requested state of the Page
     *            at load
     * @param callback
     * 
     *            Note: PageLoaders are difficult to test so minimize the logic
     *            within this class.
     * 
     */
    public void load(PageId pageId, PageState pageState,
        AsyncCallback<Page> callback);

}
