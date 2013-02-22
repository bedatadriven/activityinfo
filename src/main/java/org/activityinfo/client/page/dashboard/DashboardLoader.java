package org.activityinfo.client.page.dashboard;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateSerializer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DashboardLoader implements PageLoader {
    private final Provider<DashboardPage> welcomeProvider;

    @Inject
    public DashboardLoader(NavigationHandler pageManager,
        PageStateSerializer placeSerializer,
        Provider<DashboardPage> welcomeProvider) {
        this.welcomeProvider = welcomeProvider;

        pageManager.registerPageLoader(DashboardPage.PAGE_ID, this);
        placeSerializer.registerStatelessPlace(DashboardPage.PAGE_ID,
            new DashboardPlace());
    }

    @Override
    public void load(PageId pageId, PageState pageState,
        AsyncCallback<Page> callback) {
        callback.onSuccess(welcomeProvider.get());
    }
}