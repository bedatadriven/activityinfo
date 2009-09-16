package org.activityinfo.server.report.renderer.image;

import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.ReportElement;

import java.io.OutputStream;
import java.io.IOException;

import com.google.inject.Inject;
/*
 * @author Alex Bertram
 */

public class ImageReportRenderer implements Renderer {


    private final ImageMapRenderer renderer;

    @Inject
    public ImageReportRenderer(ImageMapRenderer renderer) {
        this.renderer = renderer;
    }

    public void render(ReportElement element, OutputStream os) throws IOException {

        if(element instanceof MapElement) {
            renderer.render((MapElement) element, os);
        }

    }

}
