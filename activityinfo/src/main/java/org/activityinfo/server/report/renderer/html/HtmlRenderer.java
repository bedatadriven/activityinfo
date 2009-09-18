package org.activityinfo.server.report.renderer.html;

import java.io.IOException;

import org.activityinfo.server.report.generator.ContentGenerator;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.model.ReportElement;

public interface HtmlRenderer<T extends ReportElement> {


	public abstract void render(HtmlWriter html, ImageStorageProvider provider, T element) throws IOException;


}
