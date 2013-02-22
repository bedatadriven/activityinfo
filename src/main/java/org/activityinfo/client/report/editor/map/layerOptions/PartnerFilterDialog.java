package org.activityinfo.client.report.editor.map.layerOptions;

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

import java.util.HashSet;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PartnerFilterDialog extends Dialog {

    private final Dispatcher service;
    private ListStore<PartnerDTO> store;
    private CheckBoxListView<PartnerDTO> listView;

    private SelectionCallback<Set<Integer>> callback;

    public PartnerFilterDialog(Dispatcher service) {
        this.service = service;

        initializeComponent();
        createList();
    }

    private void initializeComponent() {
        setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
        setWidth(250);
        setHeight(350);
        setLayout(new FitLayout());
        setScrollMode(Style.Scroll.NONE);
        setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
    }

    private void createList() {
        store = new ListStore<PartnerDTO>();
        listView = new CheckBoxListView<PartnerDTO>();
        listView.setStore(store);
        listView.setDisplayProperty("name");
        add(listView);
    }

    public Set<Integer> getSelectedIds() {
        Set<Integer> set = new HashSet<Integer>();

        for (PartnerDTO model : listView.getChecked()) {
            set.add(model.getId());
        }
        return set;
    }

    public void show(Filter baseFilter, final Filter currentFilter,
        SelectionCallback<Set<Integer>> callback) {
        show();
        this.callback = callback;
        service.execute(new GetPartnersWithSites(baseFilter),
            new AsyncCallback<PartnerResult>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(PartnerResult result) {
                    Set<Integer> ids = currentFilter
                        .getRestrictions(DimensionType.Partner);

                    store.removeAll();
                    store.add(result.getData());
                    for (PartnerDTO partner : store.getModels()) {
                        if (ids.contains(partner.getId())) {
                            listView.setChecked(partner, true);
                        }
                    }
                }
            });
    }

    @Override
    protected void onButtonPressed(Button button) {
        if (button.getItemId().equals("ok")) {
            callback.onSelected(getSelectedIds());
        }
        callback = null;
        hide();
    }
}
