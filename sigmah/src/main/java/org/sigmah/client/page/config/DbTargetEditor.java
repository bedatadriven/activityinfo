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
import org.sigmah.shared.command.AddTarget;
import org.sigmah.shared.command.RemoveProject;
import org.sigmah.shared.command.RemoveTarget;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.TargetDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.DuplicateException;
import org.sigmah.shared.exception.ProjectHasSitesException;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/*
 * Displays a grid where users can add, remove and change Targets
 */
public class DbTargetEditor extends AbstractGridPresenter<TargetDTO> {

	public static final PageId DatabaseTargets = new PageId("targets");

	@ImplementedBy(DbTargetGrid.class)
	public interface View extends GridView<DbTargetEditor, TargetDTO> {
		public void init(DbTargetEditor editor, UserDatabaseDTO db,	ListStore<TargetDTO> store);
		public void createTargetValueContainer();
		public FormDialogTether showAddDialog(TargetDTO target, UserDatabaseDTO db, FormDialogCallback callback);
	}

	private final Dispatcher service;
	private final EventBus eventBus;
	private final View view;

	private UserDatabaseDTO db;
	private ListStore<TargetDTO> store;

	@Inject
	public DbTargetEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, View view) {

		super(eventBus, stateMgr, view);
		this.service = service;
		this.eventBus = eventBus;
		this.view = view;
	}

	 public void go(UserDatabaseDTO db) {
	        this.db = db;

	        store = new ListStore<TargetDTO>();
	        store.setSortField("name");
	        store.setSortDir(Style.SortDir.ASC);
	        store.add(new ArrayList<TargetDTO>(db.getTargets()));

	        view.init(this, db, store);
	        view.setActionEnabled(UIActions.delete, false);
	        view.createTargetValueContainer();
	    }
	 
	 
	 @Override
		protected void onDeleteConfirmed(final TargetDTO target) {
	        service.execute(new RemoveTarget(db.getId(), target.getId()), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
	            public void onFailure(Throwable caught) {
	                
	            }

	            public void onSuccess(VoidResult result) {
	                store.remove(target);
	            }
	        });
		}

		@Override
		protected void onAdd() {
	        final TargetDTO newTarget = new TargetDTO();
	        this.view.showAddDialog(newTarget, db, new FormDialogCallback() {

	            @Override
	            public void onValidated(final FormDialogTether dlg) {

	                service.execute(new AddTarget(db.getId(), newTarget), dlg, new AsyncCallback<CreateResult>() {
	                    public void onFailure(Throwable caught) {
	                        if (caught instanceof DuplicateException) {
	                        	MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer(), null);
	                        } else {
	                        	MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer(), null);
	                        }
	                    }

	                    public void onSuccess(CreateResult result) {
	                    	newTarget.setId(result.getNewId());
	                        store.add(newTarget);
	                        db.getTargets().add(newTarget);
	                        eventBus.fireEvent(AppEvents.SchemaChanged);
	                        dlg.hide();
	                    }
	                });
	            }
	        });
		}
		
	@Override
	public PageId getPageId() {
		return DatabaseTargets;
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
	protected String getStateId() {
		return "TargetGrid";
	}

	@Override
	public void onSelectionChanged(ModelData selectedItem) {
		view.setActionEnabled(UIActions.delete, true);
	}


}
