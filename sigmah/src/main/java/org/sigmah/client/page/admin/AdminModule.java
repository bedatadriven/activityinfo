package org.sigmah.client.page.admin;

import com.google.gwt.inject.client.AbstractGinModule;

public class AdminModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AdminPresenter.View.class).to(AdminView.class);
	}

}
