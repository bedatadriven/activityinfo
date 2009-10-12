package org.activityinfo.server.report.renderer.html;

import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.PivotChartElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HtmlChartRendererJC extends ChartRendererJC implements HtmlChartRenderer {


	@Override
	public void render(HtmlWriter html, ImageStorageProvider imageStorageProvider, PivotChartElement element) throws IOException {
		
		/*
		 * Generate the title
		 */
		
		if(element.getTitle() != null) {
			html.header(2, element.getTitle());
		}
		
		/*
		 * If there is exactly one indicator, include the 
		 * full name of the indicator as a sub title/caption
		 */
		
//		List<Indicator> indicators = getIndicators(element);
//
//		if(indicators.size() == 1) {
//
//			html.div(indicators.get(0).getName());
//		}
		
		/*
		 * Render the image and include in the HTML file
		 */

		html.image(renderToUrl(element, false, imageStorageProvider, 600, 400, 72));
	}

	protected List<Dimension> toList(Dimension dimension) {
		if(dimension == null) {
			return Collections.emptyList();
		} else {
			List<Dimension> list = new ArrayList<Dimension>();
			list.add(dimension);
			return list;
		}
	}
}
