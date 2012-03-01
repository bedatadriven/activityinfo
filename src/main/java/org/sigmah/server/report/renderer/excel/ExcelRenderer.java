/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.excel;


import org.apache.poi.ss.usermodel.Workbook;
import org.sigmah.shared.report.model.ReportElement;


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
