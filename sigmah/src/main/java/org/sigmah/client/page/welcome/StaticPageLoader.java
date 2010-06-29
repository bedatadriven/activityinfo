package org.sigmah.client.page.welcome;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
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
