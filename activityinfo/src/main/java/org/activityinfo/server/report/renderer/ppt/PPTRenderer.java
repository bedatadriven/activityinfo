package org.activityinfo.server.report.renderer.ppt;

import com.google.inject.Inject;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.ReportElement;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Alex Bertram
 */
public class PPTRenderer implements Renderer {

    private final PPTMapRenderer mapRenderer;

    @Inject
    public PPTRenderer(PPTMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    public void render(ReportElement element, OutputStream os) throws IOException {

        if(element instanceof MapElement) {
            mapRenderer.render((MapElement) element, os);
        }
    }

    @Override
    public String getMimeType() {
        return "application/vnd.ms-powerpoint";
    }

    @Override
    public String getFileSuffix() {
        return ".ppt";
    }
}
