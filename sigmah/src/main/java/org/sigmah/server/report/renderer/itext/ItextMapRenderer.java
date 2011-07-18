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

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;


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

    public void render(DocWriter writer, Document doc, MapReportElement element) {

        try {
            doc.add(ThemeHelper.elementTitle(element.getTitle()));
            ItextRendererHelper.addFilterDescription(doc, element.getContent().getFilterDescriptions());

            renderMap(writer, element,doc);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    
    public void renderMap(DocWriter writer, MapReportElement element, Document doc) {
        try {
            BufferedImage image = new BufferedImage(element.getWidth(), element.getHeight(), ColorSpace.TYPE_RGB);
    		Graphics2D g2d = image.createGraphics();

            render(element, g2d);
            
            // Note that it is important to use JPEG here because otherwise
            // itext will decode and reencode the image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();            
            ImageIO.write(image,"JPEG", baos);

            Image imageElement = Image.getInstance(baos.toByteArray());
            imageElement.scalePercent(72f/92f*100f);

            doc.add(imageElement);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
