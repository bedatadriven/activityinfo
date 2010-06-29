package org.sigmah.client.page.config;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.AppEvents;
import org.sigmah.client.Application;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Created;
import org.sigmah.client.dispatch.callback.Deleted;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.form.DatabaseForm;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import java.util.HashMap;
import java.util.Map;

public class DbListPresenter implements ActionListener {
    public static final PageId DatabaseList = new PageId("dblist");


    public interface View  {
        void setActionEnabled(String id, boolean enabled);
        AsyncMonitor getDeletingMonitor();
    }

    private final EventBus eventBus;
    private final Dispatcher dispatcher;
    private final View view;

    private ListStore<UserDatabaseDTO> store;
    private BaseListLoader<ListLoadResult<UserDatabaseDTO>> loader;
    private UserDatabaseDTO selection;

    @Inject
    public DbListPresenter(EventBus eventBus, Dispatcher dispatcher, View view) {
        this.eventBus = eventBus;
        this.dispatcher = dispatcher;
        this.view = view;

        createLoader();
        createStore();
        loader.load();
    }

    private void createStore() {
        store = new ListStore<UserDatabaseDTO>(loader);
    }

    private void createLoader() {
        loader = new BaseListLoader<ListLoadResult<UserDatabaseDTO>>(new Proxy());
        loader.setRemoteSort(false);
    }

    public ListStore<UserDatabaseDTO> getStore() {
        return store;
    }

    public void onSelectionChanged(UserDatabaseDTO selectedItem) {
        selection = selectedItem;
        enableActions();
    }

    private void enableActions() {
        if(selection == null) {
            view.setActionEnabled(UIActions.delete, false);
            view.setActionEnabled(UIActions.edit, false);
        } else {
            view.setActionEnabled(UIActions.delete, userHasRightToDeleteSelectedDatabase());
            view.setActionEnabled(UIActions.edit, userHasRightToEditSelectedDatabase());
        }
    }

    private boolean userHasRightToDeleteSelectedDatabase() {
        return selection.getAmOwner();
    }

    private boolean userHasRightToEditSelectedDatabase() {
        return (selection.isDesignAllowed() || selection.isManageUsersAllowed());
    }

    @Override
    public void onUIAction(String actionId) {
        if(UIActions.delete.equals(actionId)) {
            onDelete();
        } else if(UIActions.edit.equals(actionId)){
            onEdit();
        } else if(UIActions.add.equals(actionId)) {
            onAdd();
        }
    }

    private void onDelete() {
        MessageBox.confirm(Application.CONSTANTS.appTitle(),
                Application.MESSAGES.confirmDeleteDb(selection.getName()),
                new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(MessageBoxEvent be) {
                        deleteSelection();
                    }
                });
    }

    private void deleteSelection() {
        dispatcher.execute(new Delete(selection), view.getDeletingMonitor(), new Deleted() {
            @Override
            public void deleted() {
                store.remove(selection);
                selection = null;
            }
        });
    }

    public void onEdit() {
        requestNavigationToDatabaseEditPage();
    }

    public void onAdd() {
        final UserDatabaseDTO db = new UserDatabaseDTO();

        DatabaseForm form = new DatabaseForm(dispatcher);
        form.getBinding().bind(db);

        final FormDialogImpl dialog = new FormDialogImpl(form);
        dialog.setWidth(400);
        dialog.setHeight(200);
        dialog.setHeading(Application.CONSTANTS.newDatabase());

        dialog.show(new FormDialogCallback() {
            @Override
            public void onValidated() {
                save(db, dialog);
            }
        });
    }

    /** Package visible for testing **/
    void save(UserDatabaseDTO db, final FormDialogTether dialog) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", db.getName());
        properties.put("fullName", db.getFullName());
        properties.put("countryId", db.getCountry().getId());

        dispatcher.execute(new CreateEntity("UserDatabase", properties), dialog, new Created() {
            @Override
            public void created(int newId) {
                eventBus.fireEvent(AppEvents.SchemaChanged);
                loader.load();
                dialog.hide();
            }
        });
    }

    private void requestNavigationToDatabaseEditPage() {
        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested,
                new DbPageState(DbConfigPresenter.DatabaseConfig, selection.getId())));
    }

    protected class Proxy implements DataProxy {
        public void load(DataReader dataReader, Object loadConfig, final AsyncCallback callback) {
            dispatcher.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }
                public void onSuccess(SchemaDTO schema) {
                    callback.onSuccess(new BaseListLoadResult<UserDatabaseDTO>(schema.getDatabases()));
                }
            });
        }
    }
}
