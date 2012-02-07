package org.sigmah.client.page.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppBar extends Composite  {

	private static AppBarUiBinder uiBinder = GWT.create(AppBarUiBinder.class);

	@UiField
	SectionTabStrip sectionTabStrip;

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

}
