package org.activityinfo.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.shared.report.model.PivotChartElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
/*
 * @author Alex Bertram
 */

public class ItextChartRenderer implements ItextRenderer<PivotChartElement> {

    private final ChartRendererJC chartRenderer;
    private static int RESOLUTION = 300;

    @Inject
    public ItextChartRenderer(ChartRendererJC chartRenderer) {
        this.chartRenderer = chartRenderer;
    }

    public void render(DocWriter writer, PivotChartElement element, Document doc) {


        try {
            doc.add(ThemeHelper.elementTitle(element.getTitle()));

            float width = doc.getPageSize().getWidth() - doc.rightMargin() - doc.leftMargin();
            float height = (doc.getPageSize().getHeight() - doc.topMargin() - doc.bottomMargin()) / 3f;

            if(writer instanceof PdfWriter) {

                // We can render the chart directly as vector graphics
                // in the PDF file

                PdfWriter pdfWriter = (PdfWriter)writer;
                PdfContentByte cb = pdfWriter.getDirectContent();
                Graphics2D g2d = cb.createGraphics(width, height);
                chartRenderer.render(element, false, g2d, (int)width, (int)height, 72);
                g2d.dispose();


            } else {

                // For RTF/Html we embed as a GIF

                width = width / 72f * RESOLUTION;
                height = height / 72f * RESOLUTION;

                BufferedImage chartImage = chartRenderer.renderImage(element, false, (int)width, (int)height, RESOLUTION);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(chartImage, "GIF", baos);

                Image image = Image.getInstance(baos.toByteArray());
                image.scalePercent(72f/RESOLUTION*100f);

                doc.add(image);

            }
           
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
