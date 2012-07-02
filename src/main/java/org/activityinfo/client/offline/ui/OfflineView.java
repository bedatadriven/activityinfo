/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline.ui;

import com.google.inject.Singleton;

/**
 * @author Alex Bertram
 */
@Singleton                                               
public class OfflineView  {
//
//	private CrossSessionStateProvider stateProvider;
//    private ProgressDialog progressDialog;
//    private ConnectionDialog connectionDialog;
//
//    private BaseObservable button = new BaseObservable();
//    private Menu menu;
//    private MenuItem syncNowButton;
//    private MenuItem toggleModeButton;
//    private MenuItem reinstallOffline;
//    
//    private Label label;
//
//    @Inject
//    public OfflineView(CrossSessionStateProvider stateProvider, OfflineCapabilityProfile profile) {
//    	this.stateProvider = stateProvider;
//        syncNowButton = new MenuItem(I18N.CONSTANTS.syncNow(), IconImageBundle.ICONS.sync());
//        toggleModeButton = new MenuItem(I18N.CONSTANTS.switchToOnline());
//        reinstallOffline = new MenuItem(I18N.CONSTANTS.reinstallOfflineMode());
//        
//        menu = new Menu();
//        menu.add(syncNowButton);
//        menu.add(toggleModeButton);
//        menu.add(new SeparatorMenuItem());
//        menu.add(reinstallOffline);
//    }
//
//	public void bindIcon(Label offlineButton) {
//		this.label = offlineButton;
//		this.label.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				menu.show(label);
//			}
//		});
//	}
//    
//    @Override
//    public Observable getButton() {
//        return button;
//    }
//    
//    public Label getLabel() {
//    	return label;
//    }
//
//    @Override
//    public Observable getSyncNowItem() {
//        return syncNowButton;
//    }
//
//    @Override
//    public Observable getToggleItem() {
//        return toggleModeButton;
//    }
//
//    @Override
//    public Observable getReinstallItem() {
//        return reinstallOffline;
//    }
//
//    @Override
//    public void setButtonTextToInstall() {
//        this.setIcon(null);
//        this.setText(I18N.CONSTANTS.installOffline());
//        toggleModeButton.setText(I18N.CONSTANTS.switchToOffline());
//    }
//
//    @Override
//    public void setButtonTextToInstalling() {
//        this.setIcon(null);
//        this.setText(I18N.CONSTANTS.installingOffline());
//    }
//
//    private void setIcon(Object object) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//    public void setButtonTextToLastSync(Date lastSyncTime) {
//        this.setIcon(IconImageBundle.ICONS.offline());
//        this.setText(I18N.MESSAGES.lastSynced(DateTimeFormat.getShortDateTimeFormat().format(lastSyncTime)));
//        toggleModeButton.setText(I18N.CONSTANTS.switchToOnline());
//    }
//
//    private void setText(String value) {
//    	///this.label.setText(value);
//	}
//
//	@Override
//    public void setButtonTextToSyncing() {
//        this.setIcon(IconImageBundle.ICONS.syncing());
//        this.setText(I18N.CONSTANTS.synchronizing());
//    }
//
//    @Override
//    public void setButtonTextToOnline() {
//        this.setIcon(IconImageBundle.ICONS.onlineSynced());
//        this.setText(I18N.CONSTANTS.online());
//        toggleModeButton.setText(I18N.CONSTANTS.switchToOffline());
//    }
//
//    @Override
//    public void setButtonTextToLoading() {
//        this.setText(I18N.CONSTANTS.loading());
//    }
//
//    @Override
//    public void showProgressDialog() {
//        if(progressDialog == null) {
//            progressDialog = new ProgressDialog();
//        }
//        progressDialog.show();
//    }
//
//    @Override
//    public void hideProgressDialog() {
//        if(progressDialog != null) {
//            progressDialog.hide();
//        }
//    }
//
//    @Override
//    public void updateProgress(String taskDescription, double percentComplete) {
//        assert progressDialog != null;
//        progressDialog.getProgressBar().updateProgress(percentComplete / 100, taskDescription);
//    }
//    
//    @Override
//	public void showConnectionProblem(int attempt, int retryDelay) {
//    	assert progressDialog != null;
//    	progressDialog.getProgressBar().updateText(I18N.CONSTANTS.connectionProblem());
//	}
//
//	@Override
//	public void enableMenu() {
//       // setMenu(menu);
//    }
//
//    @Override
//	public void disableMenu() {
//       // setMenu(null);
//    }
//
//	@Override
//	public void showError(String message) {
//		MessageBox.alert(I18N.CONSTANTS.offlineMode(), I18N.CONSTANTS.offlineInstallError() + "<br><br>" + message, null);
//
//	}
//
//	@Override
//	public void promptToGoOnline(final PromptConnectCallback callback) {
//		if(connectionDialog == null) {
//			connectionDialog = new ConnectionDialog(); 
//		}
//		connectionDialog.setCallback(callback);
//		connectionDialog.clearStatus();
//		connectionDialog.show();
//	}
//	
//	@Override
//	public void promptToLogin() {
//		if(connectionDialog == null) {
//			connectionDialog = new ConnectionDialog(); 
//		}
//		connectionDialog.setCallback(new PromptConnectCallback() {
//			
//			@Override
//			public void onTryToConnect() {				
//			}
//			
//			@Override
//			public void onCancel() {
//				connectionDialog.hide();
//			}
//		});
//		connectionDialog.setSessionExpired();
//		connectionDialog.show();
//	}
//	
//	
//
//	@Override
//	public void promptToReloadForNewVersion() {
//		MessageBox.alert(I18N.CONSTANTS.appTitle(), I18N.CONSTANTS.newVersionPrompt(), new Listener<MessageBoxEvent>() {
//			@Override
//			public void handleEvent(MessageBoxEvent be) {
//				Window.Location.reload();
//			}
//		});
//	}
//
//	@Override
//	public void setConnectionDialogToConnectionFailure() {
//		connectionDialog.setConnectionFailed();
//	}
//
//	@Override
//	public void setConnectionDialogToBusy() {
//		connectionDialog.setBusy();	
//	}
//	
//	@Override
//	public void setConnectionDialogToSessionExpired() {
//		connectionDialog.setSessionExpired();
//	}
//
//	@Override
//	public void setConnectionDialogToServerUnavailable() {
//		connectionDialog.setServerUnavailable();
//	}
//
//	@Override
//	public void hideConnectionDialog() {
//		connectionDialog.hide();
//		
//	}
//
//	@Override
//	public void promptEnable(EnableCallback callback) {
//		if(PromptOfflineDialog.shouldAskAgain(stateProvider)) {
//			PromptOfflineDialog dialog = new PromptOfflineDialog(stateProvider,callback);
//			dialog.show();
//		}
//	}
//
//	@Override
//	public void showInstallInstructions() {
//		OfflineCapabilityProfile profile = GWT.create(OfflineCapabilityProfile.class);
//		MessageBox.alert(I18N.CONSTANTS.offlineMode(), profile.getInstallInstructions(),
//				null);
//	}
//
//	@Override
//	public void showSynchronizerConnectionProblem() {
//		MessageBox.alert(I18N.CONSTANTS.syncFailed(), I18N.CONSTANTS.synchronizerConnectionProblem(), null);
//	}
//
//	public void clickButton() {
//		button.fireEvent(Events.Select, new BaseEvent(Events.Select));
//	}


}
