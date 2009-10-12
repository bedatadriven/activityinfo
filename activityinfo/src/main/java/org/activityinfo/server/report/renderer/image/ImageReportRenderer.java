package org.activityinfo.server.report.renderer.image;

import com.google.inject.Inject;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.ReportElement;

import java.io.IOException;
import java.io.OutputStream;
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

    @Override
    public String getMimeType() {
        return ".png";
    }

    @Override
    public String getFileSuffix() {
        return "image/png";
    }
}
