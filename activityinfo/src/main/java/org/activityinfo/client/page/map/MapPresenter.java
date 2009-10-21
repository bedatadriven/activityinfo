package org.activityinfo.client.page.map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.callback.DownloadCallback;
import org.activityinfo.client.command.callback.Got;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ExportCallback;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.model.ReportElement;
/*
 * @author Alex Bertram
 */

public class MapPresenter implements PagePresenter, ExportCallback, ActionListener {

    public interface View  {

        public void bindPresenter(MapPresenter presenter);

        public AsyncMonitor getSchemaLoadingMonitor();

        public AsyncMonitor getMapLoadingMonitor();

        public ReportElement getMapElement();

        void setContent(ReportElement element, Content result);

        boolean validate();
    }

    private final PageId pageId;
    private final EventBus eventBus;
    private final CommandService service;
    private final View view;

    public MapPresenter(PageId pageId, EventBus eventBus, CommandService service, final View view) {
        this.pageId = pageId;
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.view.bindPresenter(this);

    }

    public void onUIAction(String itemId) {
        if(UIActions.refresh.equals(itemId)) {
            onRefresh();
        } else if(UIActions.exportData.equals(itemId)) {
            export(RenderElement.Format.Excel_Data);
        }
    }

    public boolean navigate(Place place) {
        return false;
    }

    public void onRefresh() {

        if(view.validate()) {

            final ReportElement element = this.view.getMapElement();

            service.execute(new GenerateElement(element), view.getMapLoadingMonitor(), new AsyncCallback<Content>() {

                public void onFailure(Throwable caught) {

                }

                public void onSuccess(Content result) {
                    view.setContent(element, result);
                }
            });
        }

    }

    public PageId getPageId() {
        return pageId;
    }

    public Object getWidget() {
        return view;
    }

    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void export(RenderElement.Format format) {

        if(view.validate()) {

           service.execute(new RenderElement(view.getMapElement(), format), view.getMapLoadingMonitor(),
                   new DownloadCallback(eventBus));
        }

    }

    public void shutdown() {

    }
}