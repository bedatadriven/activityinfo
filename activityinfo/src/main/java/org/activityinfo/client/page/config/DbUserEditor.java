package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.loader.PagingCmdLoader;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.page.common.grid.GridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.state.IStateManager;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserPermissionDTO;
/*
 * @author Alex Bertram
 */

public class DbUserEditor extends AbstractEditorGridPresenter<UserPermissionDTO>
        implements GridPresenter<UserPermissionDTO> {
    public static final PageId DatabaseUsers = new PageId("dbusers");

    @ImplementedBy(DbUserGrid.class)
    public interface View extends GridView<DbUserEditor, UserPermissionDTO> {

        public void init(DbUserEditor presenter, UserDatabaseDTO db, ListStore<UserPermissionDTO> store);

        public FormDialogTether showNewForm(UserPermissionDTO user, FormDialogCallback callback);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    private UserDatabaseDTO db;

    private ListStore<UserPermissionDTO> store;
    private PagingCmdLoader<UserResult> loader;

    @Inject
    public DbUserEditor(EventBus eventBus, Dispatcher service, IStateManager stateMgr, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
    }

    public void go(UserDatabaseDTO db, DbPageState place) {
        this.db = db;

        loader = new PagingCmdLoader<UserResult>(service);
        loader.setCommand(new GetUsers(db.getId()));
        initLoaderDefaults(loader, place, new SortInfo("name", Style.SortDir.ASC));

        store = new ListStore<UserPermissionDTO>(loader);
        store.setKeyProvider(new ModelKeyProvider<UserPermissionDTO>() {
            public String getKey(UserPermissionDTO model) {
                return model.getEmail();
            }
        });

        initListeners(store, loader);

        view.init(this, db, store);
        view.setActionEnabled(UIActions.save, false);
        view.setActionEnabled(UIActions.add, db.isManageUsersAllowed());
        view.setActionEnabled(UIActions.delete, false);

        loader.load();
    }

    public void shutdown() {
    }

    @Override
    public ListStore<UserPermissionDTO> getStore() {
        return store;
    }

    @Override
    public int getPageSize() {
        return 100;
    }

    public boolean navigate(PageState place) {
        DbPageState userPlace = (DbPageState) place;

        if (userPlace.getDatabaseId() == db.getId()) {
            // internal nav
            handleGridNavigation(loader, userPlace);
            return true;
        } else {
            return false;
        }
    }

    public void onSelectionChanged(UserPermissionDTO selectedItem) {
        if (selectedItem != null) {
            view.setActionEnabled(UIActions.delete, db.isManageAllUsersAllowed() ||
                    (db.isManageUsersAllowed() && db.getMyPartnerId() == selectedItem.getPartner().getId()));
        }
        view.setActionEnabled(UIActions.delete, selectedItem != null);
    }

    @Override
    public void onDeleteConfirmed(final UserPermissionDTO model) {
        model.setAllowView(false);
        model.setAllowViewAll(false);
        model.setAllowEdit(false);
        model.setAllowEditAll(false);
        model.setAllowDesign(false);
        model.setAllowManageAllUsers(false);
        model.setAllowManageUsers(false);

        service.execute(new UpdateUserPermissions(db.getId(), model), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(VoidResult result) {
                store.remove(model);
            }
        });
    }

    public boolean validateChange(UserPermissionDTO user, String property, boolean value) {

        // If the user doesn't have the manageUser permission, then it's definitely
        // a no.
        if (!db.isManageUsersAllowed())
            return false;

        // if the user is only allowed to manager their own partners, then make
        // sure they're changing someone from their own organisation
        if (!db.isManageAllUsersAllowed() && db.getMyPartner().getId() != user.getPartner().getId())
            return false;

        // do not allow users to set rights they themselves do not have
        if ("allowViewAll".equals(property) && !db.isViewAllAllowed())
            return false;
        if ("allowEdit".equals(property) && !db.isEditAllowed())
            return false;
        if ("allowEditAll".equals(property) && !db.isEditAllAllowed())
            return false;
        if ("allowDesign".equals(property) && !db.isDesignAllowed())
            return false;
        if ("allowManageUsers".equals(property) && !db.isManageUsersAllowed())
            return false;
        if ("allowManageAllUsers".equals(property) && !db.isManageAllUsersAllowed())
            return false;

        return true;
    }

    @Override
    protected Command createSaveCommand() {

        BatchCommand batch = new BatchCommand();

        for (Record record : store.getModifiedRecords()) {
            batch.add(new UpdateUserPermissions(db.getId(), (UserPermissionDTO) record.getModel()));
        }
        return batch;
    }

    @Override
    protected void onAdd() {

        final UserPermissionDTO newUser = new UserPermissionDTO();
        newUser.setAllowView(true);

        view.showNewForm(newUser, new FormDialogCallback() {
            @Override
            public void onValidated(final FormDialogTether dlg) {
                service.execute(new UpdateUserPermissions(db, newUser), dlg, new AsyncCallback<VoidResult>() {

                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(VoidResult result) {
                        loader.load();
                        dlg.hide();
                    }
                });
            }
        });

    }

    @Override
    protected String getStateId() {
        return "User" + db.getId();
    }

    public PageId getPageId() {
        return DatabaseUsers;
    }

    public Object getWidget() {
        return view;
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void onRowEdit(String property, boolean value, Record record) {
        record.beginEdit();
        if (!value) {
            // Cascade remove permissions
            if ("allowViewAll".equals(property)) {
                record.set("allowEditAll", false);
            } else if ("allowEditAll".equals(property)) {
                record.set("allowEdit", false);
            }
        } else {
            // cascade add permissions
            if ("allowEditAll".equals(property)) {
                record.set("allowEdit", true);
            }
        }


        record.endEdit();
        onDirtyFlagChanged(store.getModifiedRecords().size() != 0);
    }

}
