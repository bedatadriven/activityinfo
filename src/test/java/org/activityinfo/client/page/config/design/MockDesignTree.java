package org.activityinfo.client.page.config.design;

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

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.ConfirmCallback;
import org.activityinfo.shared.dto.EntityDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;

public class MockDesignTree implements DesignPresenter.View {

    public ModelData selection = null;
    public Map<String, Object> newEntityProperties = new HashMap<String, Object>();

    @Override
    public void init(DesignPresenter presenter, UserDatabaseDTO db,
        TreeStore store) {

    }

    @Override
    public FormDialogTether showNewForm(EntityDTO entity,
        FormDialogCallback callback) {

        for (String property : newEntityProperties.keySet()) {
            ((ModelData) entity).set(property,
                newEntityProperties.get(property));
        }

        FormDialogTether tether = createNiceMock(FormDialogTether.class);
        replay(tether);

        callback.onValidated(tether);
        return tether;
    }

    protected void mockEditEntity(EntityDTO entity) {

    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {

    }

    @Override
    public void confirmDeleteSelected(ConfirmCallback callback) {

    }

    @Override
    public ModelData getSelection() {
        return selection;
    }

    @Override
    public AsyncMonitor getDeletingMonitor() {
        return null;
    }

    @Override
    public AsyncMonitor getSavingMonitor() {
        return null;
    }
}
