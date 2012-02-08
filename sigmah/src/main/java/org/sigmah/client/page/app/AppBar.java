package org.sigmah.client.page.app;

import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AppBar extends Composite  {

	private static AppBarUiBinder uiBinder = GWT.create(AppBarUiBinder.class);

	@UiField
	SectionTabStrip sectionTabStrip;

	@UiField
	Label logo;
	
	@UiField
	Label settingsButton;

	
	private SettingsPopup settingsPopup;
	
	public static int HEIGHT = 50;

	interface AppBarUiBinder extends UiBinder<Widget, AppBar> {
	}

	public AppBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public AppBar(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public SectionTabStrip getSectionTabStrip() {
		return sectionTabStrip;
	}

	@UiHandler("logo")
	void handleLogoClick(ClickEvent e) {
		Window.open("http://www.activityinfo.org/content/", "_top", null);
	}
	
	@UiHandler("settingsButton")
	void handleSettingsClick(ClickEvent e) {
		if(settingsPopup == null) {
			settingsPopup = new SettingsPopup();
		}
		settingsPopup.setPopupPosition(Window.getClientWidth() - SettingsPopup.WIDTH, HEIGHT-3);
		settingsPopup.show();
	}
}
