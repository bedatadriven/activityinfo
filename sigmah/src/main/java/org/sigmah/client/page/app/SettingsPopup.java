package org.sigmah.client.page.app;

import org.sigmah.client.SessionUtil;
import org.sigmah.client.authentication.ClientSideAuthProvider;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.bedatadriven.rebar.appcache.client.events.ProgressEventHandler;
import com.bedatadriven.rebar.appcache.client.events.UpdateReadyEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
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
	SpanElement cacheStatus;
	
	@UiField
	Label refreshLink;
	
	@UiField
	Label logoutLink;

	private AppCache appCache;

	public SettingsPopup() {
		setWidget(uiBinder.createAndBindUi(this));
		setAutoHideEnabled(true);
		setAutoHideOnHistoryEventsEnabled(true);
		setWidth(WIDTH + "px");
		
		versionLabel.setInnerText(VersionInfo.getRevision());
		emailLabel.setInnerText(new ClientSideAuthProvider().get().getEmail());
		
		updateAndBindAppCache();
	}

	public void updateAndBindAppCache() {
		appCache = AppCacheFactory.get();
		if(appCache.getStatus() == Status.UNSUPPORTED) {
			cacheStatus.setInnerText("AppCache not supported.");
			refreshLink.setVisible(false);
		} else {
			updateAppcacheStatusLabel();
			appCache.addUpdateReadyHandler(new UpdateReadyEventHandler() {
				
				@Override
				public void onAppCacheUpdateReady() {
					updateAppcacheStatusLabel();
				}
			});
			appCache.addProgressHandler(new ProgressEventHandler() {
				
				@Override
				public void onProgress(int filesComplete, int filesTotal) {
					int percentComplete = (int)((filesComplete * 100d) / (filesTotal * 100d));
					cacheStatus.setInnerText("Downloading new version: " + percentComplete + "% complete" );
				}
			});
		}	
	}
	
	@Override
	public void show() {
		updateAppcacheStatusLabel();
		super.show();
	}

	private void updateAppcacheStatusLabel() {
		switch(appCache.getStatus()) {
		case CHECKING:
			cacheStatus.setInnerText("Checking a new version...");
			break;
		case IDLE:
			cacheStatus.setInnerText("You are using the latest version.");
			break;
			
		case DOWNLOADING:
			cacheStatus.setInnerText("Downloading a new version...");
			break;
		
		case OBSOLETE:
			cacheStatus.setInnerText("AppCache is marked as obsolete");
			break;
		case UPDATE_READY:
			cacheStatus.setInnerText("A new version has finished downloading.");
			break;
			
		case UNCACHED:
			cacheStatus.setInnerText("Not cached.");
			break;
		}
		
		refreshLink.setVisible(appCache.getStatus() == Status.UPDATE_READY);
	}
	
	@UiHandler("logoutLink")
	public void onLogoutClicked(ClickEvent e) {
		SessionUtil.logout();
	}

}
