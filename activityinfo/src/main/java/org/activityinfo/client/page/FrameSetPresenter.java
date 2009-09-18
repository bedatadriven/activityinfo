package org.activityinfo.client.page;

import org.activityinfo.client.Place;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.page.PagePresenter;

/**
 * A PageFrame is a Page which can host one or more page within
 * internal layout regions. 
 *
 *
 * @author Alex Bertram
 *
 */
public interface FrameSetPresenter extends PagePresenter {


    /**
     * Sests the
     *
     * @param regionId
     * @param page
     */
	public void setActivePage(int regionId, PagePresenter page);

    
    public PagePresenter getActivePage(int regionId);


    public AsyncMonitor showLoadingPlaceHolder(int regionId, PageId pageId, Place loadingPlace);


}
