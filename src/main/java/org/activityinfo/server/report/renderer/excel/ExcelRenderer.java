

package org.activityinfo.server.report.renderer.excel;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
