package org.activityinfo.client.page;

import org.activityinfo.client.dispatch.AsyncMonitor;

/**
 * Page which encloses or decorates another Page
 *
 * @author Alex Bertram
 */
public interface Frame extends Page {

    /**
     * Sests the
     *
     * @param page
     */
    public void setActivePage(Page page);


    public Page getActivePage();


    public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace);


}
