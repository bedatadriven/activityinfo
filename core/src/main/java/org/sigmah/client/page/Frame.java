/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page;

import org.sigmah.client.dispatch.AsyncMonitor;

/**
 * Page which encloses or decorates another Page
 *
 * @author Alex Bertram
 */
public interface Frame extends Page {

    /**
     * Changes the enclosed page
     *
     * @param page the new active page
     */
    public void setActivePage(Page page);

    /**
     *
     * @return the current active Page
     */
    public Page getActivePage();

    /**
     * Instructs the frame to show a loading placeholder while the new active page is being loaded
     * asynchronously.
     *
     * @param pageId the pageId of the page that is being loaded
     * @param loadingPlace
     * @return
     */
    public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace);
}
