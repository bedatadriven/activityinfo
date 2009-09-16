package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.shared.report.model.MapElement;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Image;
import com.lowagie.text.DocWriter;
import com.google.inject.Inject;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
/*
 * @author Alex Bertram
 */

public class ItextMapRenderer extends ImageMapRenderer implements ItextRenderer<MapElement> {

    @Inject
    public ItextMapRenderer(@MapIconPath String mapIconPath) {
        super(mapIconPath);
    }

    public void render(DocWriter writer, MapElement element, Document doc) {

        try {
            doc.add(ThemeHelper.elementTitle(element.getTitle()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.render(element, baos);

            Image image = Image.getInstance(baos.toByteArray());
            image.scalePercent(72f/92f*100f);

            doc.add(image);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
