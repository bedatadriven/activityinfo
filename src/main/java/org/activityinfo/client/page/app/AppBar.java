package org.activityinfo.client.page.app;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.local.LocalController;
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
	
	private SettingsPopup settingsPopup;

	private EventBus eventBus;

	private LocalController offlineController;
	
	public static int HEIGHT = 50;

	interface AppBarUiBinder extends UiBinder<Widget, AppBar> {
	}
	
	@Inject
	public AppBar(EventBus eventBus, LocalController offlineController) {
		this.eventBus = eventBus;
		this.offlineController = offlineController;
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SectionTabStrip getSectionTabStrip() {
		return sectionTabStrip;
	}

	@UiHandler("logo")
	void handleLogoClick(ClickEvent e) {
		Window.open("http://about.activityinfo.org/", "_blank", null);
	}
	
	@UiHandler("settingsButton")
	void handleSettingsClick(ClickEvent e) {
		if(settingsPopup == null) {
			settingsPopup = new SettingsPopup(eventBus, offlineController);
		}
		settingsPopup.setPopupPosition(Window.getClientWidth() - SettingsPopup.WIDTH, HEIGHT-3);
		settingsPopup.show();
	}
	
	@UiHandler("searchButton")
	void handleSearchClick(ClickEvent e) {
		eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new SearchPageState()));
	}
}
