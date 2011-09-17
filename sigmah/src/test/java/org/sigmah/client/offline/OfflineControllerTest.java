/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.RemoteDispatcher;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.mock.StateManagerStub;
import org.sigmah.client.offline.OfflineController.EnableCallback;
import org.sigmah.client.offline.OfflineController.PromptConnectCallback;
import org.sigmah.client.offline.capability.WebKitCapabilityProfile;
import org.sigmah.client.offline.sync.SyncStatusEvent;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.client.util.state.CrossSessionStateProvider;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.MutatingCommand;
import org.sigmah.shared.command.result.CreateResult;

import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.Observable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provider;

public class OfflineControllerTest {
    private ViewStub view;
    private MockEventBus eventBus;
    private Provider<Synchronizer> synchronizerProvider;
    private OfflineImplStub synchronizerImpl;
    private StateManagerStub stateManager;
	private RemoteDispatcher remoteDispatcher;
    private UIConstants uiConstants;

    @Before
    public void setUp() {
        view = new ViewStub();
        eventBus = new MockEventBus();
        synchronizerImpl = new OfflineImplStub();
        synchronizerProvider = new Provider<Synchronizer>() {
            @Override
            public Synchronizer get() {
                return synchronizerImpl;
            }
        };
        stateManager = new StateManagerStub();
        remoteDispatcher = null;
        uiConstants = createMock(UIConstants.class);
    }


    @Test
    public void installationSequence() {

        // No state is set, so the presenter should assume that offline is not yet installed

        OfflineController presenter = new OfflineController(view, eventBus, remoteDispatcher,
        		synchronizerProvider, stateManager, new WebKitCapabilityProfile(), uiConstants);

        assertThat(view.defaultButtonText, equalTo("Install"));

        //  click the default button
        view.defaultButton.fireEvent(Events.Select);

        // verify that installation has been launched
        assertThat(synchronizerImpl.lastCall, equalTo("install"));

        // verify the UI is showing the status
        assertThat(view.defaultButtonText, equalTo("Installing..."));
        assertThat(view.progressDialogVisible, equalTo(true));
        assertThat(view.menuEnabled, equalTo(false));

        // status event is fired
        eventBus.fireEvent(new SyncStatusEvent("sync-task", 50));

        // verify that progress bar is updated
        assertThat(view.percentComplete, equalTo(50d));

        // signal that installation is complete
        synchronizerImpl.lastCallback.onSuccess(null);

        // verify that the UI has been updated
        assertThat(view.progressDialogVisible, equalTo(false));
        assertThat(view.defaultButtonText, equalTo("Last Sync'd: <date>"));
        assertThat(view.menuEnabled, equalTo(true));
    }

    @Test
    public void loadWhenOfflineIsEnabled() {

        stateManager.set(OfflineController.OFFLINE_MODE_KEY, OfflineController.OfflineMode.OFFLINE.toString());

        OfflineController presenter = new OfflineController(view, eventBus, 
        		remoteDispatcher, synchronizerProvider, stateManager, new WebKitCapabilityProfile(), uiConstants);

        // offline async fragment finishes loading
        synchronizerImpl.lastCallback.onSuccess(null);

        assertThat(view.defaultButtonText, equalTo("Last Sync'd: <date>"));
        assertThat(synchronizerImpl.lastCall, equalTo("validateOfflineInstalled"));
    }

    @Test
    public void reinstallWhileOffline() {

        stateManager.set(OfflineController.OFFLINE_MODE_KEY, OfflineController.OfflineMode.OFFLINE.toString());

        new OfflineController(view, eventBus, remoteDispatcher,
        		synchronizerProvider, stateManager, new WebKitCapabilityProfile(), uiConstants);

        // offline async fragment finishes loading
        synchronizerImpl.lastCallback.onSuccess(null);

        // now click the reinstall menu item
        view.reinstallItem.fireEvent(Events.Select, new MenuEvent(null));

        // we should be in install mode
        assertThat(synchronizerImpl.lastCall, equalTo("install"));
        assertThat(view.defaultButtonText, equalTo("Installing..."));
    }

    @Test
    public void synchronizerConstructorExceptionsAreCaught() {
    	
        // No state is set, so the presenter should assume that offline is not yet installed

    	Provider<Synchronizer> throwingProvider = createMock(Provider.class);
    	expect(throwingProvider.get()).andThrow(new RuntimeException("foo"));
    	replay(throwingProvider);
    	
        OfflineController presenter = new OfflineController(view, eventBus, remoteDispatcher,
        		throwingProvider, stateManager, new WebKitCapabilityProfile(), uiConstants);
        
        view.defaultButton.fireEvent(Events.Select);
        
        assertThat(view.errorMessage, equalTo("foo"));
        assertThat(view.defaultButtonText, equalTo("Install"));
    }
    
    
    @Test
    public void synchronizeAfterMutatingCommands() {
    	
        stateManager.set(OfflineController.OFFLINE_MODE_KEY, OfflineController.OfflineMode.ONLINE.toString());
        
        
        Dispatcher remoteDispatcher = createMock(Dispatcher.class);
        Capture<AsyncCallback<CreateResult>> remoteServiceCallback = new Capture<AsyncCallback<CreateResult>>();
        remoteDispatcher.execute(isA(MutatingCommand.class), anyObject(AsyncMonitor.class), capture(remoteServiceCallback));
        replay(remoteDispatcher);
        
        Synchronizer impl = createMock(Synchronizer.class);
        
        OfflineController.View view = createMock(OfflineController.View.class);
        
        // Start the controller and verify we are in online mode
 
        view.setButtonTextToLoading();
        view.setButtonTextToOnline();
        view.enableMenu();

        expect(view.getButton()).andReturn(new BaseObservable());
        expect(view.getSyncNowItem()).andReturn(new BaseObservable());
        expect(view.getToggleItem()).andReturn(new BaseObservable());
        expect(view.getReinstallItem()).andReturn(new BaseObservable());
        
        replay(view, impl);
        
        OfflineController presenter = new OfflineController(view, eventBus, 
        		remoteDispatcher, provider(impl), stateManager, new WebKitCapabilityProfile(), uiConstants);

        verify(view, impl);
        
        // Now send a mutating command to the server
        
        CreateSite command = new CreateSite();
        CreateResult expectedResult = new CreateResult(99);
        
        AsyncCallback<CreateResult> outerCallback = createMock("outerCallback", AsyncCallback.class);
        outerCallback.onSuccess(eq(expectedResult));
        replay(outerCallback);
        
        presenter.execute(command, null, outerCallback);
    	
    	// Server responds with the result...
        // Verify that a background sync starts
        
        resetToDefault(view, impl);
        
        Capture<AsyncCallback<Void>> synchronizerCallback = new Capture<AsyncCallback<Void>>();
        view.setButtonTextToSyncing();
        view.disableMenu();
        
        impl.synchronize(capture(synchronizerCallback));
    	
        replay(view, impl);

        remoteServiceCallback.getValue().onSuccess(expectedResult);

        verify(view, impl);
        
        // And next the synchronization completes successfully...
    	
        resetToDefault(view);
        view.setButtonTextToOnline();
        view.enableMenu();
        replay(view);
        
        synchronizerCallback.getValue().onSuccess(null);
    	
    	verify(view);
    }


	private Provider<Synchronizer> provider(Synchronizer impl) {
		Provider<Synchronizer> implProvider = createMock(Provider.class);
        expect(implProvider.get()).andReturn(impl);
        replay(implProvider);
		return implProvider;
	}



    
    @Test
    public void recoveryAfterOfflineLoadFails() {
    	
    	CrossSessionStateProvider stateProvider = createMock(CrossSessionStateProvider.class);
    	expect(stateProvider.getString(eq(OfflineController.OFFLINE_MODE_KEY))).andReturn(OfflineController.OfflineMode.OFFLINE.name());
    	replay(stateProvider);
    	        
        Dispatcher remoteDispatcher = createMock(Dispatcher.class);
        replay(remoteDispatcher);
        
        Synchronizer impl = createMock(Synchronizer.class);	
        impl.validateOfflineInstalled(isA(AsyncCallback.class));
        expectLastCallAndCallbackWithFailure();
        replay(impl);
       
        OfflineController.View view = createMock(OfflineController.View.class);
        
        // Start the controller and verify that after loading fails, state
        // switches back to not installed
 
        view.setButtonTextToLoading();
        view.showError(isA(String.class));
        view.setButtonTextToInstall();

        expect(view.getButton()).andReturn(new BaseObservable());
        expect(view.getSyncNowItem()).andReturn(new BaseObservable());
        expect(view.getToggleItem()).andReturn(new BaseObservable());
        expect(view.getReinstallItem()).andReturn(new BaseObservable());
        
        replay(view);
        
        OfflineController presenter = new OfflineController(view, eventBus, 
        		remoteDispatcher, provider(impl), stateProvider, new WebKitCapabilityProfile(), uiConstants);

        verify(view, impl);
    }


	private void expectLastCallAndCallbackWithFailure() {
		expectLastCall().andAnswer(new IAnswer() {

			@Override
			public Object answer() throws Throwable {
				for(Object arg : getCurrentArguments()) {
					if(arg instanceof AsyncCallback) {
						((AsyncCallback) arg).onFailure(new RuntimeException("fail!"));
					}
				}
				return null;
			}
		});
	}


	private void expectLastCallAndCallbackWithSuccess() {
		expectLastMethodAndCallbackWith(null);
	}


	private void expectLastMethodAndCallbackWith(final Object result) {
		expectLastCall().andAnswer(new IAnswer<Object>() {

			@Override
			public Object answer() throws Throwable {
				for(Object arg : getCurrentArguments()) {
					if(arg instanceof AsyncCallback) {
						((AsyncCallback) arg).onSuccess(result);
						return null;
					}
				}
				throw new IllegalArgumentException("no AsyncCallback provided");
			}
		});
	}

    private static class ViewStub implements OfflineController.View {

        public BaseObservable defaultButton = new BaseObservable();
        public String defaultButtonText;
        public boolean progressDialogVisible = false;
        public String taskDescription;
        public double percentComplete;
        public boolean menuEnabled = false;
    	public String errorMessage;

        private BaseObservable reinstallItem = new BaseObservable();
        private BaseObservable toggleItem = new BaseObservable();
        private BaseObservable syncNowItem = new BaseObservable();

        @Override
        public Observable getButton() {
            return defaultButton;
        }

        @Override
		public void promptToReloadForNewVersion() {

			
		}

		@Override
        public Observable getReinstallItem() {
            return reinstallItem;
        }

        @Override
        public Observable getToggleItem() {
            return toggleItem;
        }

        @Override
		public void showConnectionProblem(int attempt, int retryDelay) {
			// TODO Auto-generated method stub
			
		}

		@Override
        public Observable getSyncNowItem() {
            return syncNowItem;
        }

        @Override
        public void setButtonTextToInstall() {
            this.defaultButtonText = "Install";
        }

        @Override
        public void setButtonTextToInstalling() {
            this.defaultButtonText = "Installing...";
        }

        @Override
        public void setButtonTextToLoading() {
            this.defaultButtonText = "Loading...";
        }

        @Override
        public void setButtonTextToSyncing() {
            this.defaultButtonText = "Syncing...";

        }

        @Override
        public void setButtonTextToLastSync(Date lastSyncTime) {
            this.defaultButtonText = "Last Sync'd: <date>";
        }

        @Override
        public void setButtonTextToOnline() {
            this.defaultButtonText = "Online";
        }

        @Override
        public void updateProgress(String taskDescription, double percentComplete) {
            this.taskDescription = taskDescription;
            this.percentComplete = percentComplete;
        }

        @Override
        public void showProgressDialog() {
            progressDialogVisible = true;
        }

        @Override
        public void hideProgressDialog() {
            progressDialogVisible = false;
        }

        @Override
        public void enableMenu() {
            menuEnabled = true;
        }

        @Override
        public void disableMenu() {
            menuEnabled = false;
        }

		@Override
		public void showError(String message) {
			errorMessage = message;
		}

		@Override
		public void promptToGoOnline(PromptConnectCallback callback) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setConnectionDialogToConnectionFailure() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setConnectionDialogToBusy() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void hideConnectionDialog() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void promptEnable(EnableCallback callback) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void showInstallInstructions() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setConnectionDialogToSessionExpired() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setConnectionDialogToServerUnavailable() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void promptToLogin() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void showSynchronizerConnectionProblem() {
			// TODO Auto-generated method stub
			
		}

    }

    private static class OfflineImplStub implements Synchronizer {
        AsyncCallback<Void> lastCallback;
        String lastCall = "not yet been called";

        @Override
        public void install(AsyncCallback<Void> callback) {
            onCalled("install", callback);
        }

        @Override
        public void goOnline(AsyncCallback<Void> callback) {
            onCalled("goOnline", callback);
        }

        @Override
        public void synchronize(AsyncCallback<Void> callback) {
            onCalled("synchronize", callback);

        }

        @Override
        public void getLastSyncTime(AsyncCallback<Date> callback) {
            callback.onSuccess(null);
        }

        private void onCalled(String method, AsyncCallback<Void> callback) {
            lastCall = method;
            lastCallback = callback;
        }

        @Override
        public void validateOfflineInstalled(AsyncCallback<Void> callback) {
        	onCalled("validateOfflineInstalled", callback);
        }


		@Override
		public boolean canHandle(Command command) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void execute(Command command, AsyncMonitor monitor,
				AsyncCallback callback) {
			// TODO Auto-generated method stub
			
		}
    }
}
