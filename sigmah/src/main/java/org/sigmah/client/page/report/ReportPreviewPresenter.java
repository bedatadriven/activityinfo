package org.sigmah.client.page.report;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.event.DownloadEvent;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GetReportDef;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.UpdateReportDef;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.command.result.XmlResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.report.model.DateRange;

/**
 * @author Alex Bertram
 */
public class ReportPreviewPresenter implements Page, ActionListener, ExportCallback {
    public static final PageId ReportPreview = new PageId("report");

    @ImplementedBy(ReportPreview.class)
    public interface View {
        void init(ReportPreviewPresenter presenter, ReportDefinitionDTO template);

        DateRange getDateRange();

        void setPreviewHtml(String html);

        AsyncMonitor getLoadingMonitor();

        void setActionEnabled(String actionId, boolean enabled);
    }

    private final EventBus eventBus;
    private final Dispatcher service;
    private final View view;

    private ReportDefinitionDTO template;

    @Inject
    public ReportPreviewPresenter(EventBus eventBus, Dispatcher service, View view) {
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
    }

    public void go(ReportDefinitionDTO template) {
        this.template = template;
        this.view.init(this, template);

        this.view.setActionEnabled(UIActions.edit, template.getAmOwner());
    }

    public PageId getPageId() {
        return ReportPreview;
    }

    public Object getWidget() {
        return view;
    }

    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(PageState place) {
        return false;
    }

    @Override
    public void export(RenderElement.Format format) {

        StringBuilder url = new StringBuilder();
        url.append("report?auth=#AUTH#")
                .append("&id=").append(template.getId())
                .append("&format=").append(format.toString());

        DateRange range = view.getDateRange();
        if (range.getMinDate() != null) {
            url.append("&minDate=").append(range.getMinDate().getTime());
        }
        if (range.getMaxDate() != null) {
            url.append("&maxDate=").append(range.getMaxDate().getTime());
        }
        eventBus.fireEvent(new DownloadEvent("report", url.toString()));
    }

    public void onUIAction(String actionId) {
        if (UIActions.refresh.equals(actionId)) {

            RenderReportHtml command = new RenderReportHtml(template.getId(), view.getDateRange());
            service.execute(command, view.getLoadingMonitor(), new Got<HtmlResult>() {

                @Override
                public void got(HtmlResult result) {
                    view.setPreviewHtml(result.getHtml());
                }
            });

        } else if (UIActions.edit.equals(actionId)) {

            service.execute(new GetReportDef(template.getId()), view.getLoadingMonitor(), new Got<XmlResult>() {
                @Override
                public void got(XmlResult result) {

                    final ReportXmlForm form = new ReportXmlForm();
                    form.setXml(result.getResult());

                    final FormDialogImpl dlg = new FormDialogImpl(form);
                    dlg.setWidth(400);
                    dlg.setHeight(400);
                    dlg.show(new FormDialogCallback() {
                        @Override
                        public void onValidated() {

                            service.execute(new UpdateReportDef(template.getId(), form.getXml()), dlg, new AsyncCallback<VoidResult>() {
                                public void onFailure(Throwable caught) {
                                    dlg.onServerError();
                                }

                                public void onSuccess(VoidResult result) {
                                    dlg.hide();

                                }
                            });

                        }
                    });
                }
            });
        }
    }

}
