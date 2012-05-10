package org.activityinfo.client.page.app;

import org.activityinfo.client.SessionUtil;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.offline.ui.OfflineView;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SettingsPopup extends PopupPanel {

	public static final int WIDTH = 250;
	
	//private final AppUpdater updater;
	
	
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
	
	@UiField(provided = true)
	Label offlineLabel;
	
	Timer appCacheTimer;

	private OfflineView offlineView;

	
	public SettingsPopup(OfflineView offlineView) {
		this.offlineView = offlineView;
		this.offlineLabel = this.offlineView.getLabel();
		
		setWidget(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setAutoHideOnHistoryEventsEnabled(true);
		setWidth(WIDTH + "px");
		
		versionLabel.setInnerText(VersionInfo.getDisplayName());
		emailLabel.setInnerText(new ClientSideAuthProvider().get().getEmail());
	}

	
	@Override
	public void show() {
		if(appCacheTimer != null) {
			appCacheTimer.cancel();
		}
		checkForUpdates();
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
		AppCache appCache = AppCacheFactory.get();
		switch(appCache.getStatus()) {
		case UNCACHED:
		case UNSUPPORTED:
		case OBSOLETE:
		case UPDATE_READY:
			// we can just refresh and the new version will load
			Window.Location.reload();
			break;
			
		default:
			// the old version is cached, we need to completely
			// load the new version before we can proceed
			startAppCacheUpdate();
		
		}
	}
	
	private void startAppCacheUpdate() {
		refreshLink.setVisible(false);
		final AppCache appCache = AppCacheFactory.get();
		appCache.checkForUpdate();
		appCacheStatus.setInnerText(I18N.CONSTANTS.versionStartDownload());
		appCacheTimer = new Timer() {
			
			@Override
			public void run() {
				switch(appCache.getStatus()) {
				case UPDATE_READY:
					appCacheStatus.setInnerText(I18N.CONSTANTS.versionUpdateReady());
				case UNCACHED:
				case OBSOLETE:
				case UNSUPPORTED:
					refreshLink.setVisible(true);
					appCacheTimer.cancel();
					break;
				
				case CHECKING:
					break;
				
				case DOWNLOADING:
					appCacheStatus.setInnerText(I18N.CONSTANTS.versionDownloading());
					break;
		
				case IDLE:
					// since we already know there is a new version, IDLE can only
					// mean that the appcache layer failed to fetch the manifest 
					appCacheStatus.setInnerText(I18N.CONSTANTS.versionConnectionProblem());
					refreshLink.setVisible(true);
					appCacheTimer.cancel();
					break;
				}
			}
		};
		appCacheTimer.scheduleRepeating(100);
	}


	@UiHandler("logoutLink")
	public void onLogoutClicked(ClickEvent e) {
		SessionUtil.logout();
	}
	
	@UiHandler("offlineLabel") 
	public void onOfflineClicked(ClickEvent e) {
		offlineView.clickButton();
	}

}
