package org.activityinfo.client.page.app;

import java.util.Date;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.SessionUtil;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.offline.OfflineController;
import org.activityinfo.client.offline.OfflineStateChangeEvent;
import org.activityinfo.client.offline.OfflineStateChangeEvent.State;
import org.activityinfo.client.offline.sync.SyncCompleteEvent;
import org.activityinfo.client.offline.sync.SyncStatusEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SettingsPopup extends PopupPanel {

	public static final int WIDTH = 250;
	
	private static SettingsPopupUiBinder uiBinder = GWT
			.create(SettingsPopupUiBinder.class);

	interface SettingsPopupUiBinder extends UiBinder<Widget, SettingsPopup> {
	}
	
	@UiField
	SpanElement versionLabel;
	
	@UiField
	SpanElement emailLabel;
	
	
	@UiField
	SpanElement versionStatus;
	
	@UiField
	SpanElement appCacheStatus;
	
	@UiField
	Label refreshLink;
	
	@UiField
	Label logoutLink;
	
	@UiField
	Label offlineInstallLabel;
	
	@UiField
	Label offlineStatusLabel;
	
	@UiField
	Label lastSyncedLabel;
	
	Date lastSyncTime = null;
	
	@UiField
	DivElement syncRow;
	
	@UiField
	Label syncNowLabel;
	
	private boolean syncing = false;
	
	private EventBus eventBus;
	
	private OfflineStateChangeEvent.State state = State.CHECKING;

	private OfflineController offlineController;
	
	public SettingsPopup(EventBus eventBus, OfflineController offlineController) {
		this.eventBus = eventBus;
		this.offlineController = offlineController;
		
		setWidget(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setAutoHideOnHistoryEventsEnabled(true);
		setWidth(WIDTH + "px");
		
		versionLabel.setInnerText(VersionInfo.getDisplayName());
		emailLabel.setInnerText(new ClientSideAuthProvider().get().getEmail());
		
		syncRow.getStyle().setDisplay(Display.NONE);
		
		eventBus.addListener(SyncStatusEvent.TYPE, new Listener<SyncStatusEvent>() {

			@Override
			public void handleEvent(SyncStatusEvent be) {
				offlineStatusLabel.setText(be.getTask() + " " + ((int)(be.getPercentComplete())) + "%");
				syncing = true;
			}
		});	
		eventBus.addListener(SyncCompleteEvent.TYPE, new Listener<SyncCompleteEvent>() {

			@Override
			public void handleEvent(SyncCompleteEvent event) {
				syncing = false;
				syncRow.getStyle().setDisplay(Display.BLOCK);
				lastSyncTime = event.getTime();
				updateLastSyncLabel();
			}

		});
		eventBus.addListener(OfflineStateChangeEvent.TYPE, new Listener<OfflineStateChangeEvent>() {

			@Override
			public void handleEvent(OfflineStateChangeEvent be) {
				onOfflineStatusChange(be.getState());
			}
		});
		onOfflineStatusChange(offlineController.getState());
	}

	@Override
	public void show() {
		checkForUpdates();
		updateLastSyncLabel();
		if(!syncing) {
			offlineStatusLabel.setText("");
		}
		super.show();
	}
	
	/**
	 * Queries the server for the latest deployed version.
	 */
	private void checkForUpdates() {
		versionStatus.setInnerText(I18N.CONSTANTS.versionChecking());
		appCacheStatus.setInnerText("");
		refreshLink.setVisible(false);
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, "/commit.id");
		request.setCallback(new RequestCallback() {
			
			@Override
			public void onResponseReceived(Request request, Response response) {
				if(response.getStatusCode() != 200) {
					versionStatus.setInnerText(I18N.CONSTANTS.versionConnectionProblem());
				
				} else if(response.getText().startsWith(VersionInfo.getCommitId())) {
					versionStatus.setInnerText(I18N.CONSTANTS.versionLatest());
					
				} else {
					versionStatus.setInnerText(I18N.CONSTANTS.versionUpdateAvailable());
					refreshLink.setVisible(true);
				}
			}
			
			@Override
			public void onError(Request request, Throwable exception) {
				versionStatus.setInnerText(I18N.CONSTANTS.versionConnectionProblem());
			}
		});
		try {
			request.send();
		} catch (RequestException e) {
			versionStatus.setInnerText(I18N.CONSTANTS.versionConnectionProblem());
			Log.debug("Problem fetching latest version", e);
		}
	}

	
	@UiHandler("refreshLink")
	public void onRefreshLink(ClickEvent e) {

	}

	@UiHandler("logoutLink")
	public void onLogoutClicked(ClickEvent e) {
		SessionUtil.logout();
	}
	
	@UiHandler("offlineInstallLabel") 
	public void onOfflineInstallClicked(ClickEvent e) {
		switch(state) {
		case UNINSTALLED:
			offlineController.install();
		}
	}
	
	@UiHandler("syncNowLabel")
	public void onSyncNowClicked(ClickEvent e) {
		offlineController.synchronize();
	}

	private void onOfflineStatusChange(State state) {
		this.state = state;
		offlineStatusLabel.setText("");
		switch(state) {
		case UNINSTALLED:
			offlineInstallLabel.setText(I18N.CONSTANTS.installOffline());
			syncRow.getStyle().setDisplay(Display.NONE);
			break;
		case INSTALLING:
			offlineInstallLabel.setText("");
			syncRow.getStyle().setDisplay(Display.NONE);
			break;
		case INSTALLED:
			offlineInstallLabel.setText(I18N.CONSTANTS.reinstallOfflineMode());
			syncRow.getStyle().setDisplay(Display.BLOCK);
			lastSyncTime = offlineController.getLastSyncTime();
			updateLastSyncLabel();
		}
	}
	

	private void updateLastSyncLabel() {
		if(lastSyncTime != null) {
			lastSyncedLabel.setText(I18N.MESSAGES.lastSynced(formatLastSyncTime(lastSyncTime)));
		}
	}
	
	private String formatLastSyncTime(Date time) {
		long now = new Date().getTime();
		long last = time.getTime();
		
		long secondsAgo = (now-last)/1000;
		if(secondsAgo <= 60) {
			return I18N.CONSTANTS.relativeTimeMinAgo();
		}
		long minutesAgo = secondsAgo / 60;
		if(minutesAgo <= 60) {
			return I18N.MESSAGES.minutesAgo((int)minutesAgo);
		}
		long hoursAgo = minutesAgo / 60;
		if(hoursAgo <= 48) {
			return I18N.MESSAGES.hoursAgo((int)hoursAgo);
		}
		long daysAgo = hoursAgo / 24;
		return I18N.MESSAGES.daysAgo((int)daysAgo);
	}
}
