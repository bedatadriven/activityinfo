/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import java.util.ArrayList;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.grid.AbstractGridPresenter;
import org.sigmah.client.page.common.grid.GridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.AddPartner;
import org.sigmah.shared.command.RemovePartner;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.DuplicatePartnerException;
import org.sigmah.shared.exception.PartnerHasSitesException;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class DbPartnerEditor extends AbstractGridPresenter<PartnerDTO> {
    public static final PageId DatabasePartners = new PageId("partners");

    @ImplementedBy(DbPartnerGrid.class)
    public interface View extends GridView<DbPartnerEditor, PartnerDTO> {

        public void init(DbPartnerEditor editor, UserDatabaseDTO db, ListStore<PartnerDTO> store);

        public FormDialogTether showAddDialog(PartnerDTO partner, FormDialogCallback callback);
    }

    private final Dispatcher service;
    private final EventBus eventBus;
    private final View view;

    private UserDatabaseDTO db;
    private ListStore<PartnerDTO> store;


    @Inject
    public DbPartnerEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, View view) {
        super(eventBus, stateMgr, view);
        this.service = service;
        this.eventBus = eventBus;
        this.view = view;
    }

    public void go(UserDatabaseDTO db) {
        this.db = db;

        store = new ListStore<PartnerDTO>();
        store.setSortField("name");
        store.setSortDir(Style.SortDir.ASC);
        store.add(new ArrayList<PartnerDTO>(db.getPartners()));

        view.init(this, db, store);
        view.setActionEnabled(UIActions.DELETE, false);
    }

    @Override
    protected String getStateId() {
        return "PartnersGrid";
    }

//    public void onSelectionChanged(PartnerDTO selectedItem) {
//        this.view.setActionEnabled(UIActions.delete, selectedItem != null);
//    }

    @Override
    protected void onAdd() {
        final PartnerDTO newPartner = new PartnerDTO();
        this.view.showAddDialog(newPartner, new FormDialogCallback() {

            @Override
            public void onValidated(final FormDialogTether dlg) {

                service.execute(new AddPartner(db.getId(), newPartner), dlg, new AsyncCallback<CreateResult>() {
                    @Override
					public void onFailure(Throwable caught) {
                        if (caught instanceof DuplicatePartnerException) {
                        	MessageBox.alert(I18N.CONSTANTS.newPartner(), I18N.CONSTANTS.duplicatePartner(), null);
                        }
                    }

                    @Override
					public void onSuccess(CreateResult result) {
                        newPartner.setId(result.getNewId());
                        store.add(newPartner);
                        eventBus.fireEvent(AppEvents.SCHEMA_CHANGED);
                        dlg.hide();
                    }
                });
            }
        });
    }

    @Override
    protected void onDeleteConfirmed(final PartnerDTO model) {
        service.execute(new RemovePartner(db.getId(), model.getId()), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            @Override
			public void onFailure(Throwable caught) {
                if (caught instanceof PartnerHasSitesException) {
                    MessageBox.alert(I18N.CONSTANTS.removeItem(), I18N.MESSAGES.partnerHasDataWarning(model.getName()), null);
                }
            }

            @Override
			public void onSuccess(VoidResult result) {
                store.remove(model);
            }
        });

    }

    @Override
	public PageId getPageId() {
        return DatabasePartners;
    }

    @Override
	public Object getWidget() {
        return view;
    }

    @Override
	public boolean navigate(PageState place) {
        return false;
    }

    @Override
	public void shutdown() {

    }

	@Override
	public void onSelectionChanged(ModelData selectedItem) {
        this.view.setActionEnabled(UIActions.DELETE, selectedItem != null);
	}
}
