package org.sigmah.client.page.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WelcomeBanner extends Composite  {

	private static WelcomeBannerUiBinder uiBinder = GWT
			.create(WelcomeBannerUiBinder.class);

	interface WelcomeBannerUiBinder extends UiBinder<Widget, WelcomeBanner> {
	}

	public WelcomeBanner() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public WelcomeBanner(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
