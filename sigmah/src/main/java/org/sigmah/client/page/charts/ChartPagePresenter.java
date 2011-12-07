/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

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
import org.sigmah.shared.report.model.PivotChartReportElement;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPagePresenter implements Page, ActionListener, ExportCallback {

    public static final PageId PAGE_ID = new PageId("charts");

    @ImplementedBy(ChartPage.class)
    public interface View {
        void bindPresenter(ChartPagePresenter presenter);
        PivotChartReportElement getChartElement();
        AsyncMonitor getMonitor();
        void setData(PivotChartReportElement element);
    }

    private final EventBus eventBus;
    private final Dispatcher service;

    private final View view;

    @Inject
    public ChartPagePresenter(EventBus eventBus, Dispatcher service, View view) {
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.view.bindPresenter(this);
    }

    @Override
	public void shutdown() {

    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
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

    @Override
	public void onUIAction(String actionId) {

        if (UIActions.REFRESH.equals(actionId)) {

            final PivotChartReportElement element = view.getChartElement();
            if(element.getIndicators() == null || element.getIndicators().size() == 0){
            	return;
            }
            if(element.getCategoryDimensions() == null || element.getCategoryDimensions().size() == 0){
            	return;
            }
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

    @Override
	public void export(RenderElement.Format format) {

    }

    @Override
	public boolean navigate(PageState place) {
        return false;
    }
}
