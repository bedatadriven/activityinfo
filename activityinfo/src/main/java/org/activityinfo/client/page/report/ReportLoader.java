package org.activityinfo.client.page.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;

/**
 * @author Alex Bertram
 */
public class ReportLoader implements PageLoader {

    private final AppInjector injector;
    private final CommandService service;

    @Inject
    public ReportLoader(AppInjector injector, CommandService service, PageManager pageManager,
                        PlaceSerializer placeSerializer) {
        this.injector = injector;
        this.service = service;

        pageManager.registerPageLoader(Pages.ReportHome, this);
        pageManager.registerPageLoader(Pages.ReportPreview, this);

        placeSerializer.registerStatelessPlace(Pages.ReportHome, new ReportHomePlace());
        placeSerializer.registerParser(Pages.ReportPreview, new ReportPreviewPlace.Parser());
    }

    public void load(final PageId pageId, final Place place, final AsyncCallback<PagePresenter> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                if(Pages.ReportPreview.equals(pageId)) {
                    loadPreview((ReportPreviewPlace)place, callback);

                } else if(Pages.ReportHome.equals(pageId)) {

                    callback.onSuccess(injector.getReportHomePresenter());
                } else {
                    GWT.log("ReportLoader received a request it didn't know how to handle: " +
                            place.toString(), null);
                }
            }
        });


    }

    private void loadPreview(final ReportPreviewPlace place, final AsyncCallback<PagePresenter> callback) {
        service.execute(GetReportTemplates.byTemplateId(place.getReportId()), null, new AsyncCallback<ReportTemplateResult>() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);

            }

            public void onSuccess(final ReportTemplateResult result) {

                for(ReportTemplateDTO dto : result.getData()) {
                    if(dto.getId() == place.getReportId()) {
                        ReportPreviewPresenter presenter = injector.getReportPreviewPresenter();
                        presenter.go(dto);

                        callback.onSuccess(presenter);
                    }
                }

            }
        });
    }
}
