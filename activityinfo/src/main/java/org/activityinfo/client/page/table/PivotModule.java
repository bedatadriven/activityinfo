package org.activityinfo.client.page.table;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author Alex Bertram
 */
public class PivotModule extends AbstractGinModule {

    @Override
    protected void configure() {

        // assure that the loader is created upon initialization
        //bind(PivotPageLoader.class).asEagerSingleton();

        // bind views to widgets
        bind(PivotPresenter.View.class).to(PivotPage.class);
    }
}
