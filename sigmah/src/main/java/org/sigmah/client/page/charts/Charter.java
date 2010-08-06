/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.model.PivotChartElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class Charter implements Page, ActionListener, ExportCallback {

    @ImplementedBy(ChartPage.class)
    public interface View {

        public void bindPresenter(Charter presenter);

        public PivotChartElement getChartElement();

        public AsyncMonitor getMonitor();

        public void setData(PivotChartElement element);

    }

    protected final EventBus eventBus;
    protected final Dispatcher service;

    private final View view;

    @Inject
    public Charter(EventBus eventBus, Dispatcher service, View view) {
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
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void onUIAction(String actionId) {

        if (UIActions.refresh.equals(actionId)) {

            final PivotChartElement element = view.getChartElement();
            service.execute(new GenerateElement(element), view.getMonitor(),
                    new AsyncCallback<Content>() {

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.error("chart request failed", throwable);
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

    public boolean navigate(PageState place) {
        return false;
    }
}
