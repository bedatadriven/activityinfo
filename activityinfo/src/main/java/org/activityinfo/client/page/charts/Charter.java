package org.activityinfo.client.page.charts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ExportCallback;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.content.PivotChartContent;
import org.activityinfo.shared.report.model.PivotChartElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class Charter implements PagePresenter, ActionListener, ExportCallback {


    @ImplementedBy(ChartPage.class)
    public interface View {

        public void bindPresenter(Charter presenter);

        public PivotChartElement getChartElement();

        public AsyncMonitor getMonitor();

        public void setData(PivotChartElement element);

    }

    protected final EventBus eventBus;
    protected final CommandService service;

    private final View view;

    @Inject
    public Charter(EventBus eventBus, CommandService service, View view) {
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.view.bindPresenter(this);
    }

    public void shutdown() {

    }

    @Override
    public PageId getPageId() {
        return Charts.Charts;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void onUIAction(String actionId) {

        if(UIActions.refresh.equals(actionId)) {

            final PivotChartElement element = view.getChartElement();
            service.execute(new GenerateElement(element), view.getMonitor(),
                    new AsyncCallback<Content>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        GWT.log("chart request failed", throwable);
                    }

                    @Override
                    public void onSuccess(Content content) {
                        element.setContent((PivotChartContent) content);
                        view.setData(element);
                    }
                });
        }
    }

    public void export(RenderElement.Format format) {

    }

    public boolean navigate(Place place) {
        return false;
    }
}
