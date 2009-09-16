package org.activityinfo.client.page.report;

import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.callback.Got;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.common.action.ActionListener;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.common.dialog.FormDialogCallback;
import org.activityinfo.client.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.shared.command.GetReportDef;
import org.activityinfo.shared.command.RenderReportHtml;
import org.activityinfo.shared.command.UpdateReportDef;
import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.command.result.XmlResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

/*
 * @author Alex Bertram
 */

public class ReportPreviewPresenter implements PagePresenter, ActionListener {

    @ImplementedBy(ReportPreview.class)
    public interface View {
        void init(ReportPreviewPresenter presenter, ReportTemplateDTO template);
        Map<String,Object> getParameters();

        void setPreviewHtml(String html);

        AsyncMonitor getLoadingMonitor();

        void setActionEnabled(String actionId, boolean enabled);
    }

    private final CommandService service;
    private final ReportTemplateDTO template;
    private final View view;

    public ReportPreviewPresenter(CommandService service, ReportTemplateDTO template, View view) {
        this.service = service;
        this.template = template;
        this.view = view;
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

    public void onUIAction(String actionId) {
        if(UIActions.refresh.equals(actionId)) {

            RenderReportHtml command = new RenderReportHtml(template.getId(), view.getParameters());
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
