/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.excel;


import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.report.model.TableElement;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.sigmah.server.report.renderer.Renderer;

import com.google.inject.Inject;

public class ExcelReportRenderer implements ExcelRenderer<Report>, Renderer {


    private final ExcelPivotTableRenderer pivotTableRenderer;
    private final ExcelTableRenderer tableRenderer;
    private final ExcelChartRenderer chartRenderer;

    @Inject
    public ExcelReportRenderer(ExcelPivotTableRenderer pivotTableRenderer, ExcelTableRenderer tableRenderer, ExcelChartRenderer chartRenderer) {
        this.pivotTableRenderer = pivotTableRenderer;
        this.tableRenderer = tableRenderer;
        this.chartRenderer = chartRenderer;
    }

    public ExcelReportRenderer() {
        this.chartRenderer = new ExcelChartRenderer();
        this.pivotTableRenderer = new ExcelPivotTableRenderer();
        this.tableRenderer = new ExcelTableRenderer();
    }

    public void render(ReportElement element, OutputStream os) throws IOException {

		HSSFWorkbook book = new HSSFWorkbook();

        if(element instanceof Report) {
            render(book, (Report) element);
        } else if(element instanceof PivotTableReportElement) {
            pivotTableRenderer.render(book, (PivotTableReportElement) element);

        } else if(element instanceof TableElement) {
            tableRenderer.render(book, (TableElement) element);

        } else if(element instanceof PivotChartReportElement) {
            chartRenderer.render(book, (PivotChartReportElement)element);
        } 
        book.write(os);

    }

    @Override
    public String getMimeType() {
        return "application/vnd.ms-excel";
    }

    @Override
    public String getFileSuffix() {
        return ".xls";
    }

    @Override
	public void render(Workbook workbook, Report report) {
		
			
		/* 
		 * Create a worksheet for each report element
		 */
		
		for(ReportElement element : report.getElements()) {

			
			if(element instanceof PivotTableReportElement) {
				
				pivotTableRenderer.render(workbook, (PivotTableReportElement) element);
							
          	} else if(element instanceof TableElement) {
				
				tableRenderer.render(workbook, (TableElement) element);
				
			}

		}
	}
}
