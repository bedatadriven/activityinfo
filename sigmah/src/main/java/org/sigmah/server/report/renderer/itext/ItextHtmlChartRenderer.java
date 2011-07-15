package org.sigmah.server.report.renderer.itext;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.sigmah.server.report.renderer.ChartRendererJC;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;
import org.sigmah.server.report.renderer.itext.HtmlReportRenderer.MyHtmlWriter;
import org.sigmah.shared.report.model.PivotChartElement;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

public class ItextHtmlChartRenderer extends ItextChartRenderer {

	private static final float RESOLUTION = 72f;
	
	private final ChartRendererJC chartRenderer;
	private final ImageStorageProvider provider;
	
	@Inject
	public ItextHtmlChartRenderer(ChartRendererJC chartRenderer,
			ImageStorageProvider provider) {
		super();
		this.chartRenderer = chartRenderer;
		this.provider = provider;
	}
	
	
	@Override
	protected void renderImage(DocWriter writer, Document document, PivotChartElement element,
			float pageWidth, float pageHeight) throws IOException, DocumentException {
		
		float width =  pageWidth / 72f * RESOLUTION;
		float height = pageHeight / 72f * RESOLUTION;

		BufferedImage chartImage = chartRenderer.renderImage(element, false, (int)width, (int)height, (int)RESOLUTION);
		ImageStorage storage = provider.getImageUrl(".gif");
		OutputStream out = storage.getOutputStream();
		ImageIO.write(chartImage, "GIF", out);
		out.close();

		MyHtmlWriter htmlWriter = (MyHtmlWriter)writer;
		htmlWriter.writeImageAtUrl(storage.getUrl(), Image.MIDDLE, width, height);
	}
}
