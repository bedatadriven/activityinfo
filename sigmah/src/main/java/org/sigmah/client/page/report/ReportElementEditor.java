package org.sigmah.client.page.report;

import javax.xml.bind.annotation.XmlElement;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.charts.ChartPage;
import org.sigmah.client.page.map.MapPage;
import org.sigmah.client.page.table.PivotPresenter;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.TableElement;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ReportElementEditor {

	private final Dispatcher service;

	private Provider<PivotPresenter> pivotPageProvider;
	private Provider<MapPage> mapPageProvider;
	private Provider<ChartPage> chartPageProvider;

	private ReportElement reportElement;
	private Page page;

	@Inject
	public ReportElementEditor(Dispatcher service,
			Provider<PivotPresenter> pivotPageProvider,
			Provider<MapPage> mapPageProvider,
			Provider<ChartPage> chartPageProvider) {

		this.service = service;
		this.pivotPageProvider = pivotPageProvider;
		this.mapPageProvider = mapPageProvider;
		this.chartPageProvider = chartPageProvider;
	}

	public Object createEditor(ReportElement reportElement) {

		this.reportElement = reportElement;
		createEditor();

		return page.getWidget();
	}

	private void createEditor() {
		if (this.reportElement instanceof PivotChartReportElement) {
			createChart();
			((ChartPage)page).bindReportElement((PivotChartReportElement)this.reportElement);
			
		} else if (this.reportElement instanceof PivotTableReportElement) {
			createTable();
			((PivotPresenter)page).bindReportElement((PivotTableReportElement)this.reportElement);
			
		} else if (this.reportElement instanceof MapReportElement) {
			createMap();
			((MapPage)page).bindReportElement((MapReportElement)this.reportElement);
			
		} else if (this.reportElement instanceof TableElement) {
			// not sure
		} else {
			throw new RuntimeException("Unknown element type "
					+ reportElement.getClass().getName());
		}
	}

	public void createChart() {
		page = chartPageProvider.get();
	}

	public void createMap() {
		page = mapPageProvider.get();
	}

	public void createTable() {
		page = pivotPageProvider.get();
	}

	public Object getWidget() {
		return page.getWidget();
	}

	public void previewReport(int reportId) {
		generateReportPreview(reportId);
	}

	private void generateReportPreview(final int reportId) {
		RenderReportHtml command = new RenderReportHtml(reportId, null);
		service.execute(command, null, new Got<HtmlResult>() {
			@Override
			public void got(HtmlResult result) {
				// TODO display preview of this report
				// view.setPreviewHtml(result.getHtml(),
				// selectedReport.getTitle());
			}
		});
	}

}
