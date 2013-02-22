
package org.activityinfo.server.report.renderer.itext;

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


import java.util.List;

import org.activityinfo.server.util.date.DateRangeFormat;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.model.DateRange;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.teklabs.gwt.i18n.server.LocaleProxy;

/**
 * Support routines for ItextRenderers
 */
final class ItextRendererHelper {

	private ItextRendererHelper() {}

	/**
	 * Adds a set of paragraphs describing the filters which are applied to this
	 * given ReportElement.
	 * @param document
	 * @param filterDescriptions
	 * @throws DocumentException
	 */
	public static void addFilterDescription(Document document, List<FilterDescription> filterDescriptions) throws DocumentException {
		for(FilterDescription description : filterDescriptions) {
			document.add(ThemeHelper.filterDescription(description.joinLabels(", ")));
		}
	}

	public static void addDateFilterDescription(Document document, DateRange dateRange) throws DocumentException {
		if(dateRange.getMinDate() != null || dateRange.getMaxDate() != null) {
			DateRangeFormat format = new DateRangeFormat(LocaleProxy.getLocale());
			document.add(ThemeHelper.filterDescription(format.format(dateRange)));
		}
	}
}
