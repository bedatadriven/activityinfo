package org.activityinfo.client.page.report;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.callback.Got;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.common.toolbar.ExportCallback;
import org.activityinfo.shared.command.GetReportDef;
import org.activityinfo.shared.command.RenderReportHtml;
import org.activityinfo.shared.command.UpdateReportDef;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.command.result.XmlResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.date.DateRange;

/**
 * @author Alex Bertram
 */
public class ReportPreviewPresenter implements PagePresenter, ActionListener, ExportCallback {

    @ImplementedBy(ReportPreview.class)
    public interface View {
        void init(ReportPreviewPresenter presenter, ReportTemplateDTO template);
        DateRange getDateRange();

        void setPreviewHtml(String html);

        AsyncMonitor getLoadingMonitor();

        void setActionEnabled(String actionId, boolean enabled);
    }

    private final Authentication auth;
    private final CommandService service;
    private final View view;

    private ReportTemplateDTO template;

    @Inject
    public ReportPreviewPresenter(Authentication auth, CommandService service, View view) {
        this.auth = auth;
        this.service = service;
        this.view = view;
    }

    public void go(ReportTemplateDTO template) {
        this.template = template;
        this.view.init(this, template);

        this.view.setActionEnabled(UIActions.edit, template.getAmOwner());
    }

    public PageId getPageId() {
        return Pages.ReportPreview;
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

    public void shutdown() {

    }

    public boolean navigate(Place place) {
        return false;
    }

    @Override
    public void export(RenderElement.Format format) {

        StringBuilder url = new StringBuilder();
        url.append("../report?auth=").append(auth.getAuthToken())
                .append("&id=").append(template.getId())
                .append("&format=").append(format.toString());

        DateRange range = view.getDateRange();
        if(range.getMinDate() != null) {
            url.append("&from=").append(range.getMinDate().getTime());
        }
        if(range.getMaxDate() != null) {
            url.append("&to=").append(range.getMaxDate().getTime());
        }

        Window.open(url.toString(), "_downloadFrame", null);
    }

    public void onUIAction(String actionId) {
        if(UIActions.refresh.equals(actionId)) {

            RenderReportHtml command = new RenderReportHtml(template.getId(), view.getDateRange());
            service.execute(command, view.getLoadingMonitor(), new Got<HtmlResult>() {

                @Override
                public void got(HtmlResult result) {
                    view.setPreviewHtml(result.getHtml());
                }
            });

        } else if(UIActions.edit.equals(actionId)) {

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
