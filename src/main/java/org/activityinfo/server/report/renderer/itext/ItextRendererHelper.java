/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

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
