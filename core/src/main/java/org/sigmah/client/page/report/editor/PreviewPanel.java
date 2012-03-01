package org.sigmah.client.page.report.editor;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.result.HtmlResult;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class PreviewPanel extends ContentPanel {

	private final Dispatcher dispatcher;
	private Html previewHtml;

	public PreviewPanel(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		setHeaderVisible(true);
		setHeading(I18N.CONSTANTS.reportPreview());
		setScrollMode(Scroll.AUTO);
		setLayoutData(new FitData(new Margins(5, 5, 5, 5)));
		setLayout(new FitLayout());

		previewHtml = new Html();
		previewHtml.addStyleName("report");
		add(previewHtml);
	}


	public void loadPreview(int reportId) {
		RenderReportHtml command = new RenderReportHtml(reportId,
				null);
		dispatcher.execute(command, new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading()),
				new Got<HtmlResult>() {
			@Override
			public void got(HtmlResult result) {
				previewHtml.setHtml(result.getHtml());

			}
		});
	}

}
