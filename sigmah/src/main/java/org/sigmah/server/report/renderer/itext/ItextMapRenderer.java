/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.image.ImageMapRenderer;
import org.sigmah.shared.report.model.MapReportElement;

import java.io.ByteArrayOutputStream;


/**
 * Renders a {@link org.sigmah.shared.report.model.MapReportElement MapElement} into an iText
 * document
 *
 * @author Alex Bertram
 */
public class ItextMapRenderer extends ImageMapRenderer implements ItextRenderer<MapReportElement> {

    @Inject
    public ItextMapRenderer(@MapIconPath String mapIconPath) {
        super(mapIconPath);
    }

    public void renderMap(DocWriter writer, MapReportElement element, Document doc) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.render(element, baos);

            Image image = Image.getInstance(baos.toByteArray());
            image.scalePercent(72f/92f*100f);

            doc.add(image);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void render(DocWriter writer, Document doc, MapReportElement element) {

        try {
            doc.add(ThemeHelper.elementTitle(element.getTitle()));
            ItextRendererHelper.addFilterDescription(doc, element.getContent().getFilterDescriptions());

            renderMap(writer, element,doc);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
