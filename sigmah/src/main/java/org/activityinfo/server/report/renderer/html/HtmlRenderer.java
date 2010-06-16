package org.activityinfo.server.report.renderer.html;

import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.model.ReportElement;

import java.io.IOException;

public interface HtmlRenderer<T extends ReportElement> {


	public abstract void render(HtmlWriter html, ImageStorageProvider provider, T element) throws IOException;


}
