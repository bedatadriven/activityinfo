/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.image;

import java.io.IOException;
import java.io.OutputStream;

import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.ReportElement;

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
        // TODO: support for other types?

        if(element instanceof MapReportElement) {
            renderer.render((MapReportElement) element, os);
        }

    }

    @Override
    public String getMimeType() {
        return "image/png";
    }

    @Override
    public String getFileSuffix() {
        return ".png";
    }
}
