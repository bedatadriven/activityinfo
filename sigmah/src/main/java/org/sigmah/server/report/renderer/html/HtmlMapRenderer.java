/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.html;

import com.google.inject.Inject;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.image.ImageMapRenderer;
import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.report.model.MapElement;

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

        if(element.getTitle() != null) {
            html.header(1, element.getTitle());
        }

        html.image(is.getUrl()).at("width", element.getWidth()).at("height", element.getHeight());

    }
}
