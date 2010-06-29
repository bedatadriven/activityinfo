package org.sigmah.client.page;

import org.sigmah.client.page.common.Shutdownable;

/**
 * Component that models the concept of a web page within a single-page JavaScript application.
 *
 * Page instances are provided by {@link org.sigmah.client.page.PageLoader}s, which
 * handle both dependency injection, and, potentially, downloading of required js fragments
 * or required external javascript libraries (ex. Google Maps)
 *
 * New <code>Page</code> components should be registered with
 * {@link NavigationHandler#registerPageLoader(PageId, PageLoader)}
 *                                  *
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
     * The presenter can callback immediately if there is no problem, or
     * prompt the user and attempt to save.
     *
     * @param place The place to which the user has requested to navigate
     */
	public void requestToNavigateAway(PageState place, NavigationCallback callback);

    /**
     * Called just before the window closes or the user is about to
     * navigate way from the application page
     *
     * @return  null to allow the navigation to continue sans molestation or
     * a string to present to the user in protest/warning
     */
    public String beforeWindowCloses();


    /**
     * Instructs the Page to update itself to reflect the state described by the given
     * PageState object.
     *
     * The Page may return false to indicate that it is not possible to update its state
     * to that described by <code>place</code> and that a new <code>Page</code> object should
     * be created and replace this instance.
     *
     * @param place A PageState object describing the intended state of this Page
     * @return true if the navigation is possible, false if a new Page component should be created
     */
    public boolean navigate(PageState place);


}
