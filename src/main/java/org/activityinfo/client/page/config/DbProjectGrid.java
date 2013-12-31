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
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.ProjectForm;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

public class DbProjectGrid extends
    AbstractGridView<ProjectDTO, DbProjectEditor> implements
    DbProjectEditor.View {
    private final UIConstants messages;
    private final IconImageBundle icons;

    private Grid<ProjectDTO> grid;

    @Inject
    public DbProjectGrid(UIConstants messages, IconImageBundle icons) {
        this.messages = messages;
        this.icons = icons;
    }

    @Override
    protected Grid<ProjectDTO> createGridAndAddToContainer(Store store) {
        grid = new Grid<ProjectDTO>((ListStore) store, createColumnModel());
        grid.setAutoExpandColumn("description");
        grid.setLoadMask(true);

        setLayout(new FitLayout());
        add(grid);

        return grid;
    }

    protected ColumnModel createColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("name", messages.name(), 150));
        columns
            .add(new ColumnConfig("description", messages.description(), 300));

        return new ColumnModel(columns);
    }

    @Override
    protected void initToolBar() {
        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.addProject(), icons.add());
        toolBar.addButton(UIActions.EDIT, messages.edit(), icons.edit());
        toolBar.addButton(UIActions.DELETE, messages.delete(), icons.delete());
    }

    @Override
    public void init(DbProjectEditor editor, UserDatabaseDTO db,
        ListStore<ProjectDTO> store) {
        super.init(editor, store);
        this.setHeading(I18N.MESSAGES.projectsForDatabase(db.getName()));
    }

    @Override
    public FormDialogTether showAddDialog(ProjectDTO project,
        FormDialogCallback callback) {

        ProjectForm form = new ProjectForm();
        form.getBinding().bind(project);

        FormDialogImpl<ProjectForm> dlg = new FormDialogImpl<ProjectForm>(form);
        dlg.setWidth(450);
        dlg.setHeight(300);
        dlg.setHeading(messages.createProject());

        dlg.show(callback);

        return dlg;
    }

}
