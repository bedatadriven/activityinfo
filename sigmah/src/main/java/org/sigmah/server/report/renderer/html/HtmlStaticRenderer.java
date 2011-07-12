package org.sigmah.server.report.renderer.html;

import java.io.IOException;

import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.report.model.StaticReportElement;

/**
*  Render static elements in html
* 
*/
public class HtmlStaticRenderer implements HtmlRenderer<StaticReportElement> {

	@Override
	public void render(HtmlWriter html, ImageStorageProvider provider, StaticReportElement element) throws IOException {
	
		if(element.getTitle() != null) {
			html.header(2, element.getTitle());
		}
		if (element.getText() != null) {
			html.paragraph(element.getText());
		}
		if (element.getImg() != null) {
			html.image(element.getImg());
		}
	}
}