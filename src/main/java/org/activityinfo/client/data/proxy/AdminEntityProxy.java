package org.activityinfo.client.data.proxy;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminEntityProxy extends RpcProxy<ListResult<AdminEntityDTO>> {

    private final Dispatcher dispatcher;
    private final int levelId;
    private Integer parentId;

    public AdminEntityProxy(Dispatcher dispatcher, int levelId) {
        this.dispatcher = dispatcher;
        this.levelId = levelId;
    }

    public void setParentAdminEntityId(Integer id) {
        this.parentId = id;
    }

    @Override
    protected void load(Object loadConfig,
        final AsyncCallback<ListResult<AdminEntityDTO>> callback) {

        GetAdminEntities query = new GetAdminEntities(levelId);
        query.setParentId(parentId);

        dispatcher.execute(query, new AsyncCallback<AdminEntityResult>() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(AdminEntityResult result) {
                callback.onSuccess(result);
            }
        });
    }
}
