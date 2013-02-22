package org.activityinfo.client.page.config;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.Got;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.nav.Link;
import org.activityinfo.client.page.common.nav.Navigator;
import org.activityinfo.client.page.config.link.IndicatorLinkPlace;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ConfigNavigator implements Navigator {

    private final Dispatcher service;
    private final UIConstants messages;
    private final IconImageBundle icons;

    @Inject
    public ConfigNavigator(Dispatcher service, UIConstants messages,
        IconImageBundle icons) {
        this.service = service;
        this.messages = messages;
        this.icons = icons;
    }

    @Override
    public String getHeading() {
        return I18N.CONSTANTS.setup();
    }

    @Override
    public String getStateId() {
        return "configNavPanel";
    }

    @Override
    public boolean hasChildren(Link parent) {
        return parent.getPageState() instanceof DbListPageState;
    }

    @Override
    public void load(DataReader<List<Link>> dataReader, Object parent,
        AsyncCallback<List<Link>> callback) {

        if (parent == null) {

            Link dbListLink = Link
                .to(new DbListPageState())
                .labeled(messages.databases())
                .withIcon(icons.database()).build();

            Link dbLinksLink = Link
                .to(new IndicatorLinkPlace())
                .labeled(messages.linkIndicators())
                .withIcon(icons.link()).build();

            callback.onSuccess(Arrays.asList(dbListLink, dbLinksLink));

        } else {
            Link link = (Link) parent;
            if (link.getPageState() instanceof DbListPageState) {
                loadDbListAsync(callback);
            }
        }
    }

    public void loadDbListAsync(final AsyncCallback<List<Link>> callback) {
        service.execute(new GetSchema(), new Got<SchemaDTO>() {
            @Override
            public void got(SchemaDTO result) {
                loadDbList(callback, result);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    private void loadDbList(final AsyncCallback<List<Link>> callback,
        SchemaDTO result) {
        List<Link> list = new ArrayList<Link>();
        for (UserDatabaseDTO db : result.getDatabases()) {
            if (db.isDesignAllowed() || db.isManageUsersAllowed()) {
                Link link = Link
                    .to(new DbPageState(DbConfigPresenter.PAGE_ID, db.getId()))
                    .labeled(db.getName())
                    .withIcon(icons.database()).build();
                link.set("db", db);
                list.add(link);
            }
        }
        callback.onSuccess(list);
    }
}
