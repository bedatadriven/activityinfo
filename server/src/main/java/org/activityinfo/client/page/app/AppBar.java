package org.activityinfo.client.page.app;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.offline.ui.OfflineView;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.search.SearchPageState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AppBar extends Composite  {

	private static AppBarUiBinder uiBinder = GWT.create(AppBarUiBinder.class);

	@UiField
	SectionTabStrip sectionTabStrip;

	@UiField
	Label logo;
	
	@UiField
	Label settingsButton;
	
	@UiField
	Label searchButton;

	OfflineView offlineView;
	
	private SettingsPopup settingsPopup;

	private EventBus eventBus;
	
	public static int HEIGHT = 50;

	interface AppBarUiBinder extends UiBinder<Widget, AppBar> {
	}
	
	@Inject
	public AppBar(EventBus eventBus, OfflineView offlineView) {
		this.eventBus = eventBus;
		this.offlineView = offlineView;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SectionTabStrip getSectionTabStrip() {
		return sectionTabStrip;
	}

	@UiHandler("logo")
	void handleLogoClick(ClickEvent e) {
		Window.open("http://www.activityinfo.org/content/", "_blank", null);
	}
	
	@UiHandler("settingsButton")
	void handleSettingsClick(ClickEvent e) {
		if(settingsPopup == null) {
			settingsPopup = new SettingsPopup(offlineView);
		}
		settingsPopup.setPopupPosition(Window.getClientWidth() - SettingsPopup.WIDTH, HEIGHT-3);
		settingsPopup.show();
	}
	
	@UiHandler("searchButton")
	void handleSearchClick(ClickEvent e) {
		eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new SearchPageState()));
	}
}
