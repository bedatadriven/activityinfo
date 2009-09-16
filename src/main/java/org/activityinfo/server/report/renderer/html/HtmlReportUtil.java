package org.activityinfo.server.report.renderer.html;

import java.util.List;

import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.content.FilterDescription;

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
