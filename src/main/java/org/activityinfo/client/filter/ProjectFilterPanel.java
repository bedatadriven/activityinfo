package org.activityinfo.client.filter;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProjectFilterPanel extends ContentPanel implements FilterPanel {
    private ListLoader<ModelData> loader;
    private ListStore<ModelData> store;
    private CheckBoxListView<ModelData> tree;
    private final Dispatcher service;

    public ProjectFilterPanel(Dispatcher service) {
        this.service = service;
        setHeading(I18N.CONSTANTS.filterByProject());
        setIcon(IconImageBundle.ICONS.filter());

        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.NONE);
        this.setHeading(I18N.CONSTANTS.filterByProject());
        this.setIcon(IconImageBundle.ICONS.filter());

        loader = new BaseListLoader(new ProjectDataProxy());

        store = new ListStore<ModelData>(loader);

        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
            @Override
            public String getKey(ModelData model) {
                if (model instanceof ProjectDTO) {
                    return "" + ((ProjectDTO) model).getId();
                } else {
                    return "";
                }
            }
        });

        tree = new CheckBoxListView<ModelData>();
        tree.setStore(store);

        add(tree);
    }

    private class ProjectDataProxy implements DataProxy<List<ModelData>> {
        private List<ProjectDTO> allPartners = null;

        @Override
        public void load(DataReader<List<ModelData>> listDataReader, Object o,
            final AsyncCallback<List<ModelData>> callback) {

            if (allPartners == null) {
                service.execute(new GetSchema(),
                    new AsyncCallback<SchemaDTO>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            callback.onFailure(caught);
                        }

                        @Override
                        public void onSuccess(SchemaDTO result) {
                            List<UserDatabaseDTO> databases = result
                                .getDatabases();
                            ArrayList<ProjectDTO> allProjects = new ArrayList<ProjectDTO>();
                            for (UserDatabaseDTO db : databases) {
                                for (ProjectDTO p : db.getProjects()) {
                                    if (!allProjects.contains(p)) {
                                        allProjects.add(p);
                                    }
                                }
                            }
                            Collections.sort(allProjects,
                                new ProjectDTOComparator());
                            callback.onSuccess(new ArrayList<ModelData>(
                                allProjects));
                        }
                    });
            } else {
                callback.onSuccess(new ArrayList<ModelData>(allPartners));
            }
        }
    }

    private class ProjectDTOComparator implements Comparator {

        @Override
        public int compare(Object project1, Object project2) {
            String name1 = null, name2 = null;
            if (project1 instanceof PartnerDTO) {
                name1 = ((ProjectDTO) project1).getName();
            }
            if (project2 instanceof PartnerDTO) {
                name2 = ((ProjectDTO) project2).getName();
            }
            if (name1 == null) {
                name1 = "";
            }
            if (name2 == null) {
                name2 = "";
            }
            return name1.compareTo(name2);
        }
    }

    public List<ProjectDTO> getSelection() {

        List<ProjectDTO> list = new ArrayList<ProjectDTO>();

        for (ModelData model : tree.getChecked()) {
            if (model instanceof ProjectDTO) {
                list.add((ProjectDTO) model);
            }
        }
        return list;
    }

    public List<Integer> getSelectedIds() {

        List<Integer> list = new ArrayList<Integer>();

        for (ModelData model : tree.getChecked()) {
            if (model instanceof ProjectDTO) {
                list.add(((ProjectDTO) model).getId());
            }
        }
        return list;
    }

    @Override
    public Filter getValue() {
        return null;
    }

    @Override
    public void setValue(Filter value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValue(Filter value, boolean fireEvents) {
        // TODO Auto-generated method stub

    }

    @Override
    public HandlerRegistration addValueChangeHandler(
        ValueChangeHandler<Filter> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void applyBaseFilter(Filter filter) {
    }

}
