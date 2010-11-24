package org.sigmah.client.page.orgunit;

import com.google.gwt.inject.client.AbstractGinModule;

public class OrgUnitModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(OrgUnitPresenter.View.class).to(OrgUnitView.class);
    }
}
