package org.sigmah.client.page.report;

import com.google.gwt.inject.client.AbstractGinModule;
/*
 * @author Alex Bertram
 */

public class ReportModule extends AbstractGinModule {
    @Override
    protected void configure() {

        // ensure the loader is created upon initalization
        //bind(ReportLoader.class).asEagerSingleton();

        // bind views to their implementations
        bind(ReportHomePresenter.View.class).to(ReportGrid.class);
        bind(ReportPreviewPresenter.View.class).to(ReportPreview.class);

    }
}
