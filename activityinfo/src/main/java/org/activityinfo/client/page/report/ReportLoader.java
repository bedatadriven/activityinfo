package org.activityinfo.client.page.report;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.PlaceSerializer;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.common.place.SimplePlaceParser;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.inject.Inject;
/*
 * @author Alex Bertram
 */

public class ReportLoader implements PageLoader {

    private final AppInjector injector;
    private final CommandService service;

    @Inject
    public ReportLoader(AppInjector injector, CommandService service) {
        this.injector = injector;
        this.service = service;

        PageManager pageManager = injector.getPageManager();
        pageManager.registerPageLoader(Pages.ReportHome, this);
        pageManager.registerPageLoader(Pages.ReportPreview, this);

        PlaceSerializer placeSerializer = injector.getPlaceSerializer();
        placeSerializer.registerParser(Pages.ReportHome, new SimplePlaceParser(new ReportHomePlace()));
        placeSerializer.registerParser(Pages.ReportPreview, new ReportPreviewPlace.Parser());
    }

    public void load(PageId pageId, Place place, final AsyncCallback<PagePresenter> callback) {

        if(Pages.ReportPreview.equals(pageId)) {
            loadPreview((ReportPreviewPlace)place, callback);

        } else if(Pages.ReportHome.equals(pageId)) {
            callback.onSuccess(injector.getReportHomePresenter());
        } else {
            GWT.log("ReportLoader received a request it didn't know how to handle: " +
                    place.toString(), null);
        }
    }

    private void loadPreview(final ReportPreviewPlace place, final AsyncCallback<PagePresenter> callback) {
        service.execute(GetReportTemplates.byTemplateId(place.getReportId()), null, new AsyncCallback<ReportTemplateResult>() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);

            }

            public void onSuccess(final ReportTemplateResult result) {

                GWT.runAsync(new RunAsyncCallback() {

                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }
                    public void onSuccess() {

                        for(ReportTemplateDTO dto : result.getData()) {
                            if(dto.getId() == place.getReportId()) {
                                ReportPreview page = new ReportPreview();
                                ReportPreviewPresenter presenter = new ReportPreviewPresenter(service, dto, page);

                                callback.onSuccess(presenter);
                            }
                        }
                    }
                });
            }
        });
    }
}
