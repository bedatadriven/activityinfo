package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridPresenter;
import org.activityinfo.client.page.common.grid.GridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.IStateManager;
import org.activityinfo.shared.command.AddPartner;
import org.activityinfo.shared.command.RemovePartner;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.DuplicateException;
import org.activityinfo.shared.exception.PartnerHasSitesException;

import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class DbPartnerEditor extends AbstractGridPresenter<PartnerModel> {

    @ImplementedBy(DbPartnerGrid.class)
    public interface View extends GridView<DbPartnerEditor, PartnerModel> {

        public void init(DbPartnerEditor editor, UserDatabaseDTO db, ListStore<PartnerModel> store);

        public FormDialogTether showAddDialog(PartnerModel partner, FormDialogCallback callback);
    }

    private final CommandService service;
    private final EventBus eventBus;
    private final View view;

    private UserDatabaseDTO db;
    private ListStore<PartnerModel> store;


    @Inject
    public DbPartnerEditor(EventBus eventBus, CommandService service,  IStateManager stateMgr, View view) {
        super(eventBus, stateMgr, view);
        this.service = service;
        this.eventBus = eventBus;
        this.view = view;
    }

    public void go(UserDatabaseDTO db) {
        store = new ListStore<PartnerModel>();
        store.setSortField("name");
        store.setSortDir(Style.SortDir.ASC);
        store.add(new ArrayList<PartnerModel>(db.getPartners()));

        view.init(this, db, store);
        view.setActionEnabled(UIActions.delete, false);
    }

    @Override
    protected String getStateId() {
        return "PartnersGrid";
    }

    public void onSelectionChanged(PartnerModel selectedItem) {
        this.view.setActionEnabled(UIActions.delete, selectedItem != null);
    }

    @Override
    protected void onAdd() {
        final PartnerModel newPartner = new PartnerModel();
        this.view.showAddDialog(newPartner, new FormDialogCallback() {

            @Override
            public void onValidated(final FormDialogTether dlg) {

                service.execute(new AddPartner(db.getId(), newPartner), dlg, new AsyncCallback<CreateResult>() {
                    public void onFailure(Throwable caught) {
                        if(caught instanceof DuplicateException) {

                        }
                    }

                    public void onSuccess(CreateResult result) {
                        newPartner.setId(result.getNewId());
                        store.add(newPartner);
                        eventBus.fireEvent(AppEvents.SchemaChanged);
                        dlg.hide();
                    }
                });
            }
        });
    }

    @Override
    protected void onDeleteConfirmed(final PartnerModel model) {
        service.execute(new RemovePartner(db.getId(), model.getId()), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {
                if(caught instanceof PartnerHasSitesException) {
                    MessageBox.alert(Application.CONSTANTS.removeItem(), Application.MESSAGES.partnerHasDataWarning(model.getName()), null);
                }
            }

            public void onSuccess(VoidResult result) {
                store.remove(model);
            }
        });

    }

    public PageId getPageId() {
        return Pages.DatabasePartners;
    }

    public Object getWidget() {
        return view;
    }

    public boolean navigate(Place place) {
        return false;
    }

    public void shutdown() {

    }

}
