package org.activityinfo.server.report.renderer.ppt;

import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.ReportElement;

import java.io.OutputStream;
import java.io.IOException;

import com.google.inject.Inject;
/*
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
}
