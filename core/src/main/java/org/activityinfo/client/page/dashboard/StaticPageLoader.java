/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.dashboard;

import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageState;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class StaticPageLoader implements PageLoader {

    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {

        StaticPage staticPage = new StaticPage();
        staticPage.navigate((StaticPageState) pageState);
        callback.onSuccess(staticPage);
    }
}
