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

import org.activityinfo.client.page.common.Shutdownable;

/**
 * Component that models the concept of a web page within a single-page
 * JavaScript application.
 * 
 * Page instances are provided by
 * {@link org.activityinfo.client.page.PageLoader}s, which handle both
 * dependency injection, and, potentially, downloading of required js fragments
 * or required external javascript libraries (ex. Google Maps)
 * 
 * New <code>Page</code> components should be registered with
 * {@link NavigationHandler#registerPageLoader(PageId, PageLoader)} *
 * 
 * @author Alex Bertram
 */
public interface Page extends Shutdownable {

    /**
     * @return a unique identifier for this Page component
     */
    public PageId getPageId();

    /**
     * @return the Page's Widget
     */
    public Object getWidget();

    /**
     * Called following a request to move away from the page when it is active.
     * The presenter can callback immediately if there is no problem, or prompt
     * the user and attempt to save.
     * 
     * @param place
     *            The place to which the user has requested to navigate
     */
    public void requestToNavigateAway(PageState place,
        NavigationCallback callback);

    /**
     * Called just before the window closes or the user is about to navigate way
     * from the application page
     * 
     * @return null to allow the navigation to continue sans molestation or a
     *         string to present to the user in protest/warning
     */
    public String beforeWindowCloses();

    /**
     * Instructs the Page to update itself to reflect the state described by the
     * given PageState object.
     * 
     * The Page may return false to indicate that it is not possible to update
     * its state to that described by <code>place</code> and that a new
     * <code>Page</code> object should be created and replace this instance.
     * 
     * @param place
     *            A PageState object describing the intended state of this Page
     * @return true if the navigation is possible, false if a new Page component
     *         should be created
     */
    public boolean navigate(PageState place);

}
