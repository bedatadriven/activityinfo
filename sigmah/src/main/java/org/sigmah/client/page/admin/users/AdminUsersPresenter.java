package org.sigmah.client.page.admin.users;

import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.page.common.grid.ConfirmCallback;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.form.UserSigmahForm;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.shared.command.GetUsersWithProfiles;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.UserListResult;
import org.sigmah.shared.dto.UserDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Loads data for users administration screen.
 * 
 * @author nrebiai
 * 
 */
public class AdminUsersPresenter implements SubPresenter {
	
	private static boolean alert = false;
    private final Dispatcher dispatcher;
    private final UserInfo info;
    private View view;
    
    @ImplementedBy(AdminUsersView.class)
    public static abstract class View extends ContentPanel {
		
		public abstract ContentPanel getMainPanel();

		public abstract Object getSelection();
		
		public abstract MaskingAsyncMonitor getDeletingMonitor();

		public abstract void confirmDeleteSelected(ConfirmCallback confirmCallback);
			
		public abstract AdminUsersStore getAdminUsersStore();

		public abstract UserSigmahForm showNewForm(Window window, AsyncCallback<CreateResult> callback, UserDTO userToUpdate);

		public abstract MaskingAsyncMonitor getUsersLoadingMonitor();

   }
    
	public static class AdminUsersStore extends ListStore<UserDTO> {
    }
	
	public static class AdminUsersActionListener implements ActionListener {
		
		private final View view;
		private final Dispatcher dispatcher;
		
		public AdminUsersActionListener(View view, Dispatcher dispatcher){
			this.view = view;
			this.dispatcher = dispatcher;
		}

		@Override
		public void onUIAction(String actionId) {
			if (UIActions.delete.equals(actionId)) {
	            view.confirmDeleteSelected(new ConfirmCallback() {
	                public void confirmed() {
	                    onDeleteConfirmed(view.getSelection());
	                }
	            });
	        } else if (UIActions.add.equals(actionId)) {
	            onAdd();
	        }
			
		}
		
		protected void onDeleteConfirmed(Object selection) {

			/*dispatcher.execute(new UpdateEntity("User", userProperties), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
	            public void onFailure(Throwable caught) {
	            }

	            public void onSuccess(VoidResult result) {
	            	view.getAdminUsersStore().remove(model);
	            }
	        });*/
		}

		protected void onAdd() {

			final Window window = new Window();
			
			final UserSigmahForm form = view.showNewForm(window, new AsyncCallback<CreateResult>(){

				@Override
				public void onFailure(Throwable arg0) {
					window.hide();
					
				}

				@Override
				public void onSuccess(CreateResult result) {
					window.hide();
					//refresh view
					AdminUsersPresenter.refreshUserPanel(dispatcher, view);
				}			
			}, null);
			
	        
	        window.add(form);
	        window.show();

	    }
    }
	
	

	@Inject
	public AdminUsersPresenter(Dispatcher dispatcher, UserInfo info) {
		this.info = info;
        this.dispatcher = dispatcher;
        this.view = new AdminUsersView(dispatcher, info);
		        
        dispatcher.execute(new GetUsersWithProfiles(), 
        		view.getUsersLoadingMonitor(),
        		new AsyncCallback<UserListResult>() {

            @Override
            public void onFailure(Throwable arg0) {
                alertPbmData();
            }

            @Override
            public void onSuccess(UserListResult result) {
            	view.getAdminUsersStore().removeAll();
            	view.getAdminUsersStore().clearFilters();
                if (result.getList() == null || result.getList().isEmpty()) {
                    alertPbmData();
                    return;
                }
                Log.debug("AdminUsersPresenter : Adding " + result.getList().size() + " results first is " + result.getList().get(0).getName());
                view.getAdminUsersStore().add(result.getList());
                view.getAdminUsersStore().commitChanges();
            }
        });
	}

	

	private static void alertPbmData() {
        if (alert)
            return;
        alert = true;
        MessageBox.alert("Users Pbm", "Pbm getting data", null);
    }

	@Override
	public Component getView() {
		Log.debug("AdminUsersPresenter : getting view");
		if (view == null) {
			view = new AdminUsersView(dispatcher, info);
			Log.debug("AdminUsersPresenter : view is null");
		}
		return view.getMainPanel();
	}

	@Override
	public void viewDidAppear() {
		//nothing to do
	}
	
	public static void refreshUserPanel(Dispatcher dispatcher, final View view) {
		dispatcher.execute(new GetUsersWithProfiles(), 
        		view.getUsersLoadingMonitor(),
        		new AsyncCallback<UserListResult>() {

            @Override
            public void onFailure(Throwable arg0) {
                alertPbmData();
            }

            @Override
            public void onSuccess(UserListResult result) {
            	view.getAdminUsersStore().removeAll();
            	view.getAdminUsersStore().clearFilters();
                if (result.getList() == null || result.getList().isEmpty()) {
                    alertPbmData();
                    return;
                }
                Log.debug("AdminUsersPresenter : Refreshing " + result.getList().size() + " results first is " + result.getList().get(0).getName());
                view.getAdminUsersStore().add(result.getList());
                view.getAdminUsersStore().commitChanges();
            }
        });
	}



	@Override
	public void discardView() {
		// TODO Auto-generated method stub
		
	}

}
