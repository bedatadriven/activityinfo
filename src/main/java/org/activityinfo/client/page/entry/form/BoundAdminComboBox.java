package org.activityinfo.client.page.entry.form;

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

import org.activityinfo.client.page.entry.admin.AdminComboBox;
import org.activityinfo.client.page.entry.admin.AdminComboBoxSet.ComboBoxFactory;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class BoundAdminComboBox extends ComboBox<AdminEntityDTO> implements
    AdminComboBox {

    public BoundAdminComboBox(AdminLevelDTO level,
        ListStore<AdminEntityDTO> store) {
        setFieldLabel(level.getName());
        setStore(store);
        setTypeAhead(false);
        setForceSelection(true);
        setEditable(false);
        setValueField("id");
        setUseQueryCache(false);
        setDisplayField("name");
        setAllowBlank(false);
        setTriggerAction(TriggerAction.ALL);
    }

    @Override
    public void addSelectionChangeListener(
        Listener<SelectionChangedEvent> listener) {
        addListener(Events.SelectionChange, listener);
    }

    public static class Factory implements ComboBoxFactory {

        @Override
        public AdminComboBox create(AdminLevelDTO level,
            ListStore<AdminEntityDTO> store) {
            return new BoundAdminComboBox(level, store);
        }
    }

}
