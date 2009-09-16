package org.activityinfo.client.page.config;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.loader.PagingCmdLoader;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.common.dialog.FormDialogCallback;
import org.activityinfo.client.common.dialog.FormDialogTether;
import org.activityinfo.client.common.grid.AbstractEditorGridPresenter;
import org.activityinfo.client.common.grid.GridPresenter;
import org.activityinfo.client.common.grid.GridView;
import org.activityinfo.client.page.*;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.*;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserModel;

import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.ImplementedBy;
/*
 * @author Alex Bertram
 */

public class DbUserEditor extends AbstractEditorGridPresenter<UserModel>
        implements GridPresenter<UserModel> {

    @ImplementedBy(DbUserGrid.class)
    public interface View extends GridView<DbUserEditor, UserModel> {

        public void init(DbUserEditor presenter, UserDatabaseDTO db, ListStore<UserModel> store);

        public FormDialogTether showNewForm(UserModel user, FormDialogCallback callback);
    }

    private final EventBus eventBus;
    private final CommandService service;
    private final View view;

    private UserDatabaseDTO db;

    private ListStore<UserModel> store;
    private PagingCmdLoader<UserResult> loader;

    @Inject
    public DbUserEditor(EventBus eventBus, CommandService service, IStateManager stateMgr, View view) {
        super(eventBus, service, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
    }

    public void go(UserDatabaseDTO db, DbPlace place) {
        this.db = db;

        loader = new PagingCmdLoader<UserResult>(service);
        loader.setCommand(new GetUsers(db.getId()));
        initLoaderDefaults(loader, place, new SortInfo("name", Style.SortDir.ASC));

        store = new ListStore<UserModel>(loader);
        store.setKeyProvider(new ModelKeyProvider<UserModel>() {
            public String getKey(UserModel model) {
                return model.getEmail();
            }
        });
        initListeners(store, loader);

        view.init(this, db, store);
        view.setActionEnabled(UIActions.save, false);
        view.setActionEnabled(UIActions.delete, false);

        loader.load();
    }


    public void shutdown() {

    }

    @Override
    public ListStore<UserModel> getStore() {
        return store;
    }

    @Override
    public int getPageSize() {
        return 100;
    }

    public boolean navigate(Place place) {


        DbPlace userPlace = (DbPlace)place;

        if(userPlace.getDatabaseId() == db.getId()) {
            // internal nav
            handleGridNavigation(loader, userPlace);
            return true;
        } else {
           return false;
        }
    }


    public void onSelectionChanged(UserModel selectedItem) {
        view.setActionEnabled(UIActions.delete, selectedItem!=null);
    }


    @Override
    public void onDeleteConfirmed(final UserModel model) {
        model.setAllowView(false);
        model.setAllowViewAll(false);
        model.setAllowEdit(false);
        model.setAllowEditAll(false);
        model.setAllowDesign(false);

        service.execute(new UpdateUser(db.getId(), model), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                store.remove(model);
            }
        });
    }

    @Override
    protected Command createSaveCommand() {

        BatchCommand batch = new BatchCommand();

        for(Record record : store.getModifiedRecords()) {
            batch.add(new UpdateUser(db.getId(), (UserModel)record.getModel()));
        }

        return batch;
    }

    @Override
    protected void onAdd() {

        final UserModel newUser = new UserModel();
        newUser.setAllowView(true);

        view.showNewForm(newUser, new FormDialogCallback() {
            @Override
            public void onValidated(FormDialogTether dlg) {
                service.execute(new UpdateUser(db, newUser), dlg, new AsyncCallback<VoidResult>() {

                    public void onFailure(Throwable caught) {

                    }

                    public void onSuccess(VoidResult result) {
                        loader.load();
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
        return Pages.DatabaseUsers;
    }

    public Object getWidget() {
        return view;
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void onRowEdit(String property, boolean value, Record record) {

        record.beginEdit();

        if(!value) {
            // Cascade remove permissions

            if("allowViewAll".equals(property)) {
                record.set("allowEditAll", false);
            } else if("allowEditAll".equals(property)) {
                record.set("allowEdit", false);
            }
        } else {

            // cascade add permissions

            if("allowEditAll".equals(property)) {
                record.set("allowEdit", true);
            }
        }

        record.endEdit();

        onDirtyFlagChanged(store.getModifiedRecords().size() != 0);

    }

}
