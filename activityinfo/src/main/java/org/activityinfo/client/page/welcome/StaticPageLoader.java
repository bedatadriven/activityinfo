package org.activityinfo.client.page.welcome;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageState;
/*
 * @author Alex Bertram
 */

public class StaticPageLoader implements PageLoader {

    public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback) {

        StaticPage staticPage = new StaticPage();
        staticPage.navigate((StaticPageState) pageState);
        callback.onSuccess(staticPage);
    }
}
