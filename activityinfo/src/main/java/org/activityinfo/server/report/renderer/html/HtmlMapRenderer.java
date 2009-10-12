package org.activityinfo.server.report.renderer.html;

import com.google.inject.Inject;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.model.MapElement;

import java.io.IOException;
import java.io.OutputStream;
/*
 * @author Alex Bertram
 */

public class HtmlMapRenderer extends ImageMapRenderer implements HtmlRenderer<MapElement> {

    @Inject
    public HtmlMapRenderer(@MapIconPath String mapIconPath) {
        super(mapIconPath);
    }

    public void render(HtmlWriter html, ImageStorageProvider provider, MapElement element) throws IOException {

        ImageStorage is = provider.getImageUrl("png");

        OutputStream out = is.getOutputStream();
        this.render(element, out);
        out.close();

        html.header(1, element.getTitle());
        html.image(is.getUrl()).at("width", element.getWidth()).at("height", element.getHeight());

    }
}
