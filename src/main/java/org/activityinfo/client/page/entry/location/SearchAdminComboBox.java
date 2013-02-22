package org.activityinfo.client.page.entry.location;

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
import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.El.VisMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class SearchAdminComboBox extends ComboBox<AdminEntityDTO> implements
    AdminComboBox {

    private El clearSpan;
    private final AdminLevelDTO level;

    public SearchAdminComboBox(AdminLevelDTO level,
        ListStore<AdminEntityDTO> store) {
        this.level = level;
        setFieldLabel(level.getName());
        setStore(store);
        setTypeAhead(false);
        setForceSelection(true);
        setEditable(false);
        setValueField("id");
        setUseQueryCache(false);
        setDisplayField("name");
        setTriggerAction(TriggerAction.ALL);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        clearSpan = new El(DOM.createSpan());
        clearSpan.setInnerHtml("clear");
        clearSpan.addStyleName(SiteFormResources.INSTANCE.style()
            .adminClearSpan());
        clearSpan.addEventsSunk(Event.MOUSEEVENTS);
        clearSpan.setVisibilityMode(VisMode.VISIBILITY);
        clearSpan.setVisible(false);

        getElement().appendChild(clearSpan.dom);
    }

    public AdminLevelDTO getLevel() {
        return level;
    }

    @Override
    public void setValue(AdminEntityDTO value) {
        super.setValue(value);

        this.clearSpan.setVisible(this.value != null);
    }

    @Override
    protected void onClick(ComponentEvent ce) {
        if (clearSpan.dom.isOrHasChild(ce.getTarget())) {
            setValue(null);
        }
        super.onClick(ce);
    }

    @Override
    protected void onKeyDown(FieldEvent fe) {
        super.onKeyDown(fe);
        if (fe.getKeyCode() == KeyCodes.KEY_ESCAPE) {
            setValue(null);
        }
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
            return new SearchAdminComboBox(level, store);
        }
    }
}
