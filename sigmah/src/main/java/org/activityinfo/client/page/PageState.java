package org.activityinfo.client.page;

import java.util.List;

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

}
