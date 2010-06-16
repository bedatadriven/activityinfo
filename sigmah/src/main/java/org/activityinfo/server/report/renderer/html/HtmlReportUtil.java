package org.activityinfo.server.report.renderer.html;

import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.content.FilterDescription;

import java.util.List;

public class HtmlReportUtil {
	
	public static void generateFilterDescriptionHtml(HtmlWriter html, List<FilterDescription> descs) {
		
		for(FilterDescription desc : descs) {
			html.div()
				.styleName(CssStyles.FILTER_DESCRIPTION)
				.styleName(CssStyles.DIMENSION(desc.getDimensionType()))
				.text( desc.getLabels(), ", ");
		}
	}
}
