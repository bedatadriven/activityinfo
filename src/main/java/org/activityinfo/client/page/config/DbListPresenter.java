/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.Created;
import org.activityinfo.client.dispatch.callback.Deleted;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.DatabaseForm;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DbListPresenter implements ActionListener {
    public static final PageId PAGE_ID = new PageId("dblist");


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
            view.setActionEnabled(UIActions.DELETE, false);
            view.setActionEnabled(UIActions.EDIT, false);
        } else {
            view.setActionEnabled(UIActions.DELETE, userHasRightToDeleteSelectedDatabase());
            view.setActionEnabled(UIActions.EDIT, userHasRightToEditSelectedDatabase());
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
        if(UIActions.DELETE.equals(actionId)) {
            onDelete();
        } else if(UIActions.EDIT.equals(actionId)){
            onEdit();
        } else if(UIActions.ADD.equals(actionId)) {
            onAdd();
        }
    }

    private void onDelete() {
        MessageBox.confirm(I18N.CONSTANTS.appTitle(),
                I18N.MESSAGES.confirmDeleteDb(selection.getName()),
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
        dialog.setHeading(I18N.CONSTANTS.newDatabase());

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
                eventBus.fireEvent(AppEvents.SCHEMA_CHANGED);
                loader.load();
                dialog.hide();
            }
        });
    }

    private void requestNavigationToDatabaseEditPage() {
        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested,
                new DbPageState(DbConfigPresenter.PAGE_ID, selection.getId())));
    }

    protected class Proxy implements DataProxy {
        public void load(DataReader dataReader, Object loadConfig, final AsyncCallback callback) {
            dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
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
