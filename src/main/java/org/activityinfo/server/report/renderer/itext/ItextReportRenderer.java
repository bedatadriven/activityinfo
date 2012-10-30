/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.activityinfo.server.geo.AdminGeometryProvider;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.image.ImageCreator;
import org.activityinfo.shared.report.model.ImageReportElement;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.report.model.TableElement;
import org.activityinfo.shared.report.model.TextReportElement;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;


/**
 * Base class for iText-based {@link org.activityinfo.shared.report.model.Report} renderers.
 * Subclasses ({@link org.activityinfo.server.report.renderer.itext.PdfReportRenderer PdfReportRenderer},
 * {@link org.activityinfo.server.report.renderer.itext.RtfReportRenderer RtfReportRenderer} target
 * specific output formats.
 *
 * @author Alex Bertram
 */
public abstract class ItextReportRenderer implements Renderer {

	private final Map<Class, ItextRenderer> renderers = new HashMap<Class, ItextRenderer>();
	
    @Inject
    protected ItextReportRenderer(AdminGeometryProvider geometryProvider, @MapIconPath String mapIconPath) {    	
    	ItextMapRenderer itextMapRenderer = new ItextMapRenderer(geometryProvider, mapIconPath, getImageCreator());
		
    	renderers.put(PivotTableReportElement.class, new ItextPivotTableRenderer());
    	renderers.put(PivotChartReportElement.class, new ItextChartRenderer(getImageCreator()));
    	renderers.put(MapReportElement.class, itextMapRenderer);
    	renderers.put(TableElement.class, new ItextTableRenderer(itextMapRenderer));
    	renderers.put(TextReportElement.class, new ItextTextRenderer());
    	renderers.put(ImageReportElement.class, new ItextImageRenderer());
    }

	@Override
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

    protected abstract ImageCreator getImageCreator();

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
        ItextRendererHelper.addDateFilterDescription(document, report.getFilter().getDateRange());

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
