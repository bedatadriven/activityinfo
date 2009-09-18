package org.activityinfo.client.page;

import org.activityinfo.client.Place;
import org.activityinfo.client.page.base.Shutdownable;

public interface PagePresenter extends Shutdownable {

   
	public PageId getPageId();
	
	public Object getWidget();

    /**
     * Called following a request to move away from the page when it is active.
     * The presenter can callback immediately if there is no problem, or
     * prompt the user and attempt to save.
     */
	public void requestToNavigateAway(Place place, NavigationCallback callback);

    /**
     * Called just before the window closes or the user is about to
     * navigate way from the application page
     *
     * @return  null to allow the navigation to continue sans molestation or
     * a string to present to the user in protest/warning
     */
    public String beforeWindowCloses();


    public boolean navigate(Place place);


}
