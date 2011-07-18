/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.shared.report.model.ImageReportElement;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.TableElement;
import org.sigmah.shared.report.model.TextReportElement;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;


/**
 * Base class for iText-based {@link org.sigmah.shared.report.model.Report} renderers.
 * Subclasses ({@link org.sigmah.server.report.renderer.itext.PdfReportRenderer PdfReportRenderer},
 * {@link org.sigmah.server.report.renderer.itext.RtfReportRenderer RtfReportRenderer} target
 * specific output formats.
 *
 * @author Alex Bertram
 */
public abstract class ItextReportRenderer implements Renderer {

	private final Map<Class, ItextRenderer> renderers = new HashMap<Class, ItextRenderer>();
	
    @Inject
    protected ItextReportRenderer(ItextChartRenderer chartRenderer, @MapIconPath String mapIconPath) {
    	ItextMapRenderer itextMapRenderer = new ItextMapRenderer(mapIconPath);
		
    	renderers.put(PivotTableReportElement.class, new ItextPivotTableRenderer());
    	renderers.put(PivotChartReportElement.class, chartRenderer);
    	renderers.put(MapReportElement.class, itextMapRenderer);
    	renderers.put(TableElement.class, new ItextTableRenderer(itextMapRenderer));
    	renderers.put(TextReportElement.class, new ItextTextRenderer());
    	renderers.put(ImageReportElement.class, new ItextImageRenderer());
    }

    public void render(ReportElement element, OutputStream os) throws IOException {

        try {
            Document document = new Document();
            DocWriter writer = createWriter(document, os);
            document.open();
            
            renderFooter(document);
            
            if(element instanceof Report) {
                renderReport(writer, document, element);
            } else {
                renderElement(writer, document, element);
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	protected abstract void renderFooter(Document document);

    /**
     * Provides a DocWriter for an open document and OutputStream. Subclasses should provide
     * an implementation for their specific output format.
     *
     * @param document
     * @param os
     * @return
     * @throws DocumentException
     */
    protected abstract DocWriter createWriter(Document document, OutputStream os) throws DocumentException;


    private void renderReport(DocWriter writer, Document document, ReportElement element) throws DocumentException {
        Report report = (Report) element;
        document.add(ThemeHelper.reportTitle(report.getTitle()));
        ItextRendererHelper.addFilterDescription(document, report.getContent().getFilterDescriptions());

        for(ReportElement childElement : report.getElements()) {
        	renderElement(writer, document, childElement);
        }
    }

	private void renderElement(DocWriter writer, Document document,
			ReportElement element) throws DocumentException {
		if(renderers.containsKey(element.getClass())) {
			ItextRenderer renderer = renderers.get(element.getClass());
		    renderer.render(writer, document, element);
		}
	}
}
