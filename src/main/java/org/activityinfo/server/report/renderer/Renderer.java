package org.activityinfo.server.report.renderer;

import org.activityinfo.shared.report.model.ReportElement;

import java.io.OutputStream;
import java.io.IOException;
/*
 * @author Alex Bertram
 */

public interface Renderer {

    public void render(ReportElement element, OutputStream os) throws IOException;
}
