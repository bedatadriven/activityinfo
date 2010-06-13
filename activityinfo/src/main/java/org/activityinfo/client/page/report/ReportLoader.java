package org.activityinfo.client.page.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportDefinitionDTO;

/**
 * @author Alex Bertram
 */
public class ReportLoader implements PageLoader {

    private final AppInjector injector;
    private final Dispatcher service;

    @Inject
    public ReportLoader(AppInjector injector, Dispatcher service, NavigationHandler pageManager,
                        PageStateSerializer placeSerializer) {
        this.injector = injector;
        this.service = service;

        pageManager.registerPageLoader(ReportHomePresenter.ReportHome, this);
        pageManager.registerPageLoader(ReportPreviewPresenter.ReportPreview, this);

        placeSerializer.registerStatelessPlace(ReportHomePresenter.ReportHome, new ReportHomePageState());
        placeSerializer.registerParser(ReportPreviewPresenter.ReportPreview, new ReportPreviewPageState.Parser());
    }

    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                if (ReportPreviewPresenter.ReportPreview.equals(pageId)) {
                    loadPreview((ReportPreviewPageState) pageState, callback);

                } else if (ReportHomePresenter.ReportHome.equals(pageId)) {

                    callback.onSuccess(injector.getReportHomePresenter());
                } else {
                    GWT.log("ReportLoader received a request it didn't know how to handle: " +
                            pageState.toString(), null);
                }
            }
        });


    }

    private void loadPreview(final ReportPreviewPageState place, final AsyncCallback<Page> callback) {
        service.execute(GetReportTemplates.byTemplateId(place.getReportId()), null, new AsyncCallback<ReportTemplateResult>() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);

            }

            public void onSuccess(final ReportTemplateResult result) {

                for (ReportDefinitionDTO dto : result.getData()) {
                    if (dto.getId() == place.getReportId()) {
                        ReportPreviewPresenter presenter = injector.getReportPreviewPresenter();
                        presenter.go(dto);

                        callback.onSuccess(presenter);
                    }
                }

            }
        });
    }
}
