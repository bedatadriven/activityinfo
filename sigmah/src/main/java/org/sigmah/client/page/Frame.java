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
     * Sests the
     *
     * @param page
     */
    public void setActivePage(Page page);


    public Page getActivePage();


    public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace);


}
