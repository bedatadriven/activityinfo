package org.activityinfo.server.report.renderer.excel;


import org.activityinfo.shared.report.model.ReportElement;
import org.apache.poi.ss.usermodel.Workbook;


public interface ExcelRenderer<T extends ReportElement> {

	/**
	 * Generate content within the given workbook based on the provided report element.
	 * 
	 * Most report elements should add a new sheet to the workbook 
	 * 
	 * @param workbook
	 * @param element
	 */
	void render(Workbook workbook, T element);

}
