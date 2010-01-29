package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageManager;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;

public class DbListPresenter extends AbstractGridPresenter<UserDatabaseDTO> implements PagePresenter {

    @ImplementedBy(DbListGrid.class)
    public interface View extends GridView<DbListPresenter, UserDatabaseDTO> {

        public void init(DbListPresenter presenter, Store store);

        public FormDialogTether showAddDialog(UserDatabaseDTO db, FormDialogCallback callback);

    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    private ListStore<UserDatabaseDTO> store;
    private BaseListLoader<ListLoadResult<UserDatabaseDTO>> loader;

    @Inject
    public DbListPresenter(EventBus eventBus, Dispatcher service, IStateManager stateMgr, View view) {
        super(eventBus, stateMgr, view);
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;

        loader = new BaseListLoader<ListLoadResult<UserDatabaseDTO>>(new Proxy());
        loader.setRemoteSort(false);

        store = new ListStore<UserDatabaseDTO>(loader);

        this.view.init(this, store);
        this.view.setActionEnabled(UIActions.delete, false);
        this.view.setActionEnabled(UIActions.edit, false);

        loader.load();
    }

    public boolean navigate(Place place) {
        return true;
    }

    @Override
    public PageId getPageId() {
        return Pages.DatabaseList;
    }

    @Override
    protected String getStateId() {
        return "DbList";
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void onSelectionChanged(UserDatabaseDTO selectedItem) {
        this.view.setActionEnabled(UIActions.delete, selectedItem != null && selectedItem.getAmOwner());
        this.view.setActionEnabled(UIActions.edit, selectedItem != null &&
                (selectedItem.isDesignAllowed() || selectedItem.isManageUsersAllowed()));
    }

    @Override
    public void onDeleteConfirmed(final UserDatabaseDTO model) {
        service.execute(new Delete(model), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {

            public void onFailure(Throwable caught) {

            }

            public void onSuccess(VoidResult result) {
                store.remove(model);
            }
        });
    }

    @Override
    public void onAdd() {

        final UserDatabaseDTO db = new UserDatabaseDTO();

        this.view.showAddDialog(db, new FormDialogCallback() {
            @Override
            public void onValidated(final FormDialogTether dlg) {

                service.execute(new CreateEntity(db), dlg, new AsyncCallback<CreateResult>() {
                    public void onFailure(Throwable caught) {

                    }

                    public void onSuccess(CreateResult result) {
                        eventBus.fireEvent(AppEvents.SchemaChanged);
                        loader.load();
                        dlg.hide();
                    }
                });

            }
        });
    }

    @Override
    public void onEdit(UserDatabaseDTO model) {
        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new DbPlace(Pages.DatabaseConfig, model.getId())));
    }

    protected class Proxy implements DataProxy {

        public void load(DataReader dataReader, Object loadConfig, final AsyncCallback callback) {

            service.execute(new GetSchema(), null, new AsyncCallback<Schema>() {

                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(Schema schema) {
                    callback.onSuccess(new BaseListLoadResult<UserDatabaseDTO>(schema.getDatabases()));
                }
            });

        }
    }

    public void shutdown() {

    }

}
