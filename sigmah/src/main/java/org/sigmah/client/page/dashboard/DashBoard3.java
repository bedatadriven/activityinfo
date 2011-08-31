package org.sigmah.client.page.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class DashBoard3 extends Composite implements HasText {

	private static DashBoard3UiBinder uiBinder = GWT
			.create(DashBoard3UiBinder.class);

	interface DashBoard3UiBinder extends UiBinder<Widget, DashBoard3> {
	}

	public DashBoard3() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DashBoard3(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return "";
	}

}
