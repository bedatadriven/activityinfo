/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
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
import org.sigmah.shared.report.model.ReportElement;

/**
 * Map page Presenter
 *
 * @author Alex Bertram
 */
public class MapPresenter implements Page, ExportCallback, ActionListener {

    public static final PageId PAGE_ID = new PageId("maps");

    @ImplementedBy(MapPage.class)
    public interface View {
        public void bindPresenter(MapPresenter presenter);
        public AsyncMonitor getMapLoadingMonitor();
        public ReportElement getMapElement();
        void setContent(ReportElement element, Content result);
        boolean validate();
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    @Inject
    public MapPresenter(EventBus eventBus, Dispatcher service, final View view) {
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.view.bindPresenter(this);
    }

    public void onUIAction(String itemId) {
        if (UIActions.refresh.equals(itemId)) {
            onRefresh();
        } else if (UIActions.exportData.equals(itemId)) {
            export(RenderElement.Format.Excel_Data);
        }
    }

    @Override
    public boolean navigate(PageState place) {
        return false;
    }

    private void onRefresh() {
        if (view.validate()) {
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
    public void export(RenderElement.Format format) {
        if (view.validate()) {
            service.execute(new RenderElement(view.getMapElement(), format), view.getMapLoadingMonitor(),
                    new DownloadCallback(eventBus, "map"));
        }
    }

    @Override
    public void shutdown() {
    }
}