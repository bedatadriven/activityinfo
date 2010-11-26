package org.sigmah.client.page.dashboard;

import com.google.gwt.inject.client.AbstractGinModule;

public class DashboardModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(DashboardPresenter.View.class).to(DashboardView.class);
    }
}
