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

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.UserForm;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserPermissionDTO;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DbUserEditor extends ContentPanel implements DbPage,
    ActionListener {
    public static final PageId PAGE_ID = new PageId("dbusers");

    private final EventBus eventBus;
    private final Dispatcher dispatcher;

    private PagingLoader<UserResult> loader;
    private ListStore<UserPermissionDTO> store;
    private Grid<UserPermissionDTO> grid;

    private UserDatabaseDTO db;

    private ActionToolBar toolBar;

    @Inject
    public DbUserEditor(EventBus eventBus, Dispatcher service,
        StateProvider stateMgr) {
        this.eventBus = eventBus;
        this.dispatcher = service;

        setHeading(I18N.CONSTANTS.users());
        setLayout(new FitLayout());

        createToolBar();
        createGrid();
        createPagingToolBar();
    }

    @Override
    public void go(UserDatabaseDTO db) {
        this.db = db;
        store.removeAll();

        toolBar.setActionEnabled(UIActions.SAVE, false);
        toolBar.setActionEnabled(UIActions.ADD, db.isManageUsersAllowed());
        toolBar.setActionEnabled(UIActions.DELETE, false);

        grid.getColumnModel().getColumnById("allowViewAll")
            .setHidden(!db.isManageUsersAllowed());
        grid.getColumnModel().getColumnById("allowEditAll")
            .setHidden(!db.isManageUsersAllowed());
        grid.getColumnModel().getColumnById("allowManageUsers")
            .setHidden(!db.isManageAllUsersAllowed());
        grid.getColumnModel().getColumnById("allowManageAllUsers")
            .setHidden(!db.isManageAllUsersAllowed());
        grid.getColumnModel().getColumnById("allowDesign")
            .setHidden(!db.isDesignAllowed());

        loader.load();
    }

    private void createToolBar() {
        toolBar = new ActionToolBar(this);
        toolBar.addSaveSplitButton();
        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.addUser(),
            IconImageBundle.ICONS.addUser());
        toolBar.addButton(UIActions.DELETE, I18N.CONSTANTS.delete(),
            IconImageBundle.ICONS.deleteUser());
        toolBar.addButton(UIActions.EXPORT, I18N.CONSTANTS.export(),
            IconImageBundle.ICONS.excel());
        toolBar.addButton(UIActions.MAILING_LIST,
            I18N.CONSTANTS.CopyAddressToClipBoard(),
            IconImageBundle.ICONS.dataEntry());
        setTopComponent(toolBar);
    }

    private void createGrid() {

        loader = new BasePagingLoader<UserResult>(new UserProxy());

        store = new ListStore<UserPermissionDTO>(loader);
        store.setKeyProvider(new ModelKeyProvider<UserPermissionDTO>() {
            @Override
            public String getKey(UserPermissionDTO model) {
                return model.getEmail();
            }
        });
        store.addListener(Store.Update,
            new Listener<StoreEvent<UserPermissionDTO>>() {

                @Override
                public void handleEvent(StoreEvent<UserPermissionDTO> event) {
                    toolBar.setDirty(!store.getModifiedRecords().isEmpty());
                }
            });

        final List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("name", I18N.CONSTANTS.name(), 100));
        columns.add(new ColumnConfig("email", I18N.CONSTANTS.email(), 150));
        columns.add(new ColumnConfig("partner", I18N.CONSTANTS.partner(), 150));

        PermissionCheckConfig allowView = new PermissionCheckConfig(
            "allowViewSimple", I18N.CONSTANTS.allowView(), 75);
        allowView.setDataIndex("allowView");
        allowView.setToolTip(I18N.CONSTANTS.allowViewLong());
        columns.add(allowView);

        PermissionCheckConfig allowEdit = new PermissionCheckConfig(
            "allowEditSimple", I18N.CONSTANTS.allowEdit(), 75);
        allowEdit.setDataIndex("allowEdit");
        allowEdit.setToolTip(I18N.CONSTANTS.allowEditLong());
        columns.add(allowEdit);

        PermissionCheckConfig allowViewAll = new PermissionCheckConfig(
            "allowViewAll", I18N.CONSTANTS.allowViewAll(), 75);
        allowViewAll.setToolTip(I18N.CONSTANTS.allowViewAllLong());
        columns.add(allowViewAll);

        PermissionCheckConfig allowEditAll = new PermissionCheckConfig(
            "allowEditAll", I18N.CONSTANTS.allowEditAll(), 75);
        allowEditAll.setToolTip(I18N.CONSTANTS.allowEditAllLong());
        columns.add(allowEditAll);

        PermissionCheckConfig allowManageUsers = null;
        allowManageUsers = new PermissionCheckConfig("allowManageUsers",
            I18N.CONSTANTS.allowManageUsers(), 150);
        columns.add(allowManageUsers);

        PermissionCheckConfig allowManageAllUsers = new PermissionCheckConfig(
            "allowManageAllUsers", I18N.CONSTANTS.manageAllUsers(), 150);
        columns.add(allowManageAllUsers);

        // only users with the right to design them selves can change the design
        // attribute
        PermissionCheckConfig allowDesign = new PermissionCheckConfig(
            "allowDesign", I18N.CONSTANTS.allowDesign(), 75);
        allowDesign.setToolTip(I18N.CONSTANTS.allowDesignLong());
        columns.add(allowDesign);

        grid = new Grid<UserPermissionDTO>(store, new ColumnModel(columns));
        grid.setLoadMask(true);
        grid.setSelectionModel(new GridSelectionModel<UserPermissionDTO>());
        grid.getSelectionModel().addSelectionChangedListener(
            new SelectionChangedListener<UserPermissionDTO>() {

                @Override
                public void selectionChanged(
                    SelectionChangedEvent<UserPermissionDTO> se) {
                    onSelectionChanged(se.getSelectedItem());
                }
            });
        grid.addPlugin(allowEdit);
        grid.addPlugin(allowViewAll);
        grid.addPlugin(allowEditAll);
        grid.addPlugin(allowManageUsers);
        grid.addPlugin(allowManageAllUsers);
        grid.addPlugin(allowDesign);
        add(grid);
    }

    private void createPagingToolBar() {
        PagingToolBar pagingToolBar = new PagingToolBar(100);
        pagingToolBar.bind(loader);
        setBottomComponent(pagingToolBar);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean navigate(PageState place) {
        return false;
    }

    private boolean validateChange(UserPermissionDTO user, String property) {

        // If the user doesn't have the manageUser permission, then it's
        // definitely
        // a no.
        if (!db.isManageUsersAllowed()) {
            return false;
        }

        // if the user is only allowed to manager their own partners, then make
        // sure they're changing someone from their own organisation
        if (!db.isManageAllUsersAllowed()
            && db.getMyPartner().getId() != user.getPartner().getId()) {
            return false;
        }

        // do not allow users to set rights they themselves do not have
        if ("allowViewAll".equals(property) && !db.isViewAllAllowed()) {
            return false;
        }
        if ("allowEdit".equals(property) && !db.isEditAllowed()) {
            return false;
        }
        if ("allowEditAll".equals(property) && !db.isEditAllAllowed()) {
            return false;
        }
        if ("allowDesign".equals(property) && !db.isDesignAllowed()) {
            return false;
        }
        if ("allowManageUsers".equals(property) && !db.isManageUsersAllowed()) {
            return false;
        }
        if ("allowManageAllUsers".equals(property)
            && !db.isManageAllUsersAllowed()) {
            return false;
        }

        return true;
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return this;
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    private void onRowEdit(String property, boolean value, Record record) {
        record.beginEdit();
        if (!value) {
            // Cascade remove permissions
            if ("allowViewAll".equals(property)) {
                record.set("allowEditAll", false);
            }
        } else {
            // cascade add permissions
            if ("allowEditAll".equals(property)) {
                record.set("allowEdit", true);
                record.set("allowViewAll", true);
            } else if ("allowManageAllUsers".equals(property)) {
                record.set("allowManageUsers", true);
            }
        }

        record.endEdit();
        toolBar.setDirty(store.getModifiedRecords().size() != 0);
    }

    private void onSelectionChanged(UserPermissionDTO selectedItem) {

        if (selectedItem != null) {
            PartnerDTO selectedPartner = selectedItem.getPartner();

            toolBar
                .setActionEnabled(
                    UIActions.DELETE,
                    db.isManageAllUsersAllowed()
                        || (db.isManageUsersAllowed() && db.getMyPartnerId() == selectedPartner
                            .getId()));
        }
        toolBar.setActionEnabled(UIActions.DELETE, selectedItem != null);
    }

    @Override
    public void onUIAction(String actionId) {
        if (actionId.equals(UIActions.SAVE)) {
            save();
        } else if (actionId.equals(UIActions.ADD)) {
            add();
        } else if (actionId.equals(UIActions.DELETE)) {
            delete();
        } else if (actionId.equals(UIActions.EXPORT)) {
            export();
        } else if (UIActions.MAILING_LIST.equals(actionId)) {
            createMailingListPopup();
        }
    }

    private void save() {
        BatchCommand batch = new BatchCommand();

        for (Record record : store.getModifiedRecords()) {
            batch.add(new UpdateUserPermissions(db.getId(),
                (UserPermissionDTO) record.getModel()));
        }

        dispatcher.execute(batch,
            new MaskingAsyncMonitor(this, I18N.CONSTANTS.saving()),
            new AsyncCallback<BatchResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    // handled by monitor
                }

                @Override
                public void onSuccess(BatchResult result) {
                    store.commitChanges();
                }
            });
    }

    private void createMailingListPopup() {
        new MailingListDialog(dispatcher, db.getId());
    }

    private void add() {
        final UserForm form = new UserForm(db);

        final FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setHeading(I18N.CONSTANTS.newUser());
        dlg.setWidth(400);
        dlg.setHeight(300);

        dlg.show(new FormDialogCallback() {

            @Override
            public void onValidated() {
                dispatcher.execute(
                    new UpdateUserPermissions(db, form.getUser()), dlg,
                    new AsyncCallback<VoidResult>() {

                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(VoidResult result) {
                            loader.load();
                            dlg.hide();
                        }
                    });
            }
        });
    }

    private void delete() {
        final UserPermissionDTO model = grid.getSelectionModel()
            .getSelectedItem();
        model.setAllowView(false);
        model.setAllowViewAll(false);
        model.setAllowEdit(false);
        model.setAllowEditAll(false);
        model.setAllowDesign(false);
        model.setAllowManageAllUsers(false);
        model.setAllowManageUsers(false);

        dispatcher.execute(new UpdateUserPermissions(db.getId(), model),
            new MaskingAsyncMonitor(this, I18N.CONSTANTS.deleting()),
            new AsyncCallback<VoidResult>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(VoidResult result) {
                    store.remove(model);
                }
            });
    }

    private void export() {
        Window.open(
            GWT.getModuleBaseURL() + "export/users?dbUsers=" + db.getId(),
            "_blank", null);
    }

    @Override
    public void requestToNavigateAway(PageState place,
        NavigationCallback callback) {
        callback.onDecided(true);
    }

    private class PermissionCheckConfig extends CheckColumnConfig {

        public PermissionCheckConfig(String id, String name, int width) {
            super(id, name, width);
        }

        @Override
        protected void onMouseDown(GridEvent<ModelData> ge) {
            El el = ge.getTargetEl();
            if (el != null && el.hasStyleName("x-grid3-cc-" + getId())
                && !el.hasStyleName("x-grid3-check-col-disabled")) {
                ge.stopEvent();
                UserPermissionDTO m = (UserPermissionDTO) ge.getModel();
                String property = grid.getColumnModel().getColumnId(
                    ge.getColIndex());
                Record r = store.getRecord(m);
                Boolean b = (Boolean) m.get(getDataIndex());
                if (validateChange(m, property)) {
                    boolean newValue = b == null ? true : !b;
                    r.set(getDataIndex(), newValue);
                    onRowEdit(property, newValue, r);
                }
            }
        }
    }

    private class UserProxy extends RpcProxy<UserResult> {

        @Override
        protected void load(Object loadConfig,
            final AsyncCallback<UserResult> callback) {

            PagingLoadConfig config = (PagingLoadConfig) loadConfig;
            GetUsers command = new GetUsers(db.getId());
            command.setOffset(config.getOffset());
            command.setLimit(config.getLimit());
            command.setSortInfo(config.getSortInfo());
            dispatcher.execute(command, callback);
        }
    }
}
