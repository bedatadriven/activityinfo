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

import java.util.Arrays;

import org.activityinfo.client.page.entry.form.resources.SiteFormResources;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;

public class FormNavigationListView extends ListView<FormSectionModel> {

    private ListStore<FormSectionModel> store;

    public FormNavigationListView() {

        store = new ListStore<FormSectionModel>();

        setStore(store);
        setTemplate(SiteFormResources.INSTANCE.formNavigationTemplate()
            .getText());
        setItemSelector(".formSec");

        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public void addSection(FormSectionModel model) {
        store.add(model);
        if (store.getCount() == 1) {
            select(model);
        }
    }

    private void select(FormSectionModel model) {
        getSelectionModel().setSelection(Arrays.asList(model));
    }

    private void select(int index) {
        select(store.getAt(index));
    }

    private void selectAndFire(int index) {
        select(index);
        ListViewEvent<FormSectionModel> event = new ListViewEvent<FormSectionModel>(
            this);
        event.setModel(getSelectionModel().getSelectedItem());
    }

    public void prev() {
        if (!getSelectionModel().getSelection().isEmpty()) {
            int index = store.indexOf(getSelectionModel().getSelectedItem());
            if (index > 0) {
                selectAndFire(index - 1);
            }
        }
    }

    public void next() {
        if (!getSelectionModel().getSelection().isEmpty()) {
            int index = store.indexOf(getSelectionModel().getSelectedItem());
            if (index + 1 < store.getCount()) {
                selectAndFire(index + 1);
            }
        }
    }
}
