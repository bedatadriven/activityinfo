package org.activityinfo.client;

import org.activityinfo.client.page.PageId;

import java.util.List;

/**
 * A "Place" is an object which describes the a user destination in
 * terms of the Page as well as the state of the page.
 *
 * @author Alex Bertram
 */
public interface Place {

    /**
     *  Returns the application-wide unique id for the page component.
     *  This should correspond to the
     */
	PageId getPageId();

    String pageStateToken();

	List<ViewPath.Node> getViewPath();


}
