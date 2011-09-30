package org.sigmah.client.page.dashboard;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetReportTemplates;
import org.sigmah.shared.command.result.ReportTemplateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Reports extends VerticalPanel {

	public Reports(Dispatcher service) {
		super();
		GetReportTemplates getReports = new GetReportTemplates();
		service.execute(getReports, null, new AsyncCallback<ReportTemplateResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: handle failure
			}

			@Override
			public void onSuccess(ReportTemplateResult result) {
				for (ReportDefinitionDTO report : result.getData()) {
					if (!isNullOrEmpty(report.getTitle())) {
						HorizontalPanel panel = new HorizontalPanel();
						panel.setSpacing(5);
						panel.add(IconImageBundle.ICONS.report().createImage());
						panel.add(new Hyperlink(report.getTitle(), "report/" + report.getId()));
						add(panel);
					}
				}
			}
		});
	}

}
