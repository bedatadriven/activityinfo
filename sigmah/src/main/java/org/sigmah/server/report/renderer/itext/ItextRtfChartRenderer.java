package org.sigmah.server.report.renderer.itext;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.sigmah.server.report.renderer.ChartRendererJC;
import org.sigmah.shared.report.model.PivotChartElement;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;

public class ItextRtfChartRenderer extends ItextChartRenderer {

	public static final float RESOLUTION = 150;
	
	private final ChartRendererJC chartRenderer;
	
	@Inject
	public ItextRtfChartRenderer(ChartRendererJC chartRenderer) {
		super();
		this.chartRenderer = chartRenderer;
	}


	@Override
	protected void renderImage(DocWriter writer, Document doc,
			PivotChartElement element, float pageWidth, float pageHeight) throws Exception {
		
		float width =  pageWidth / 72f * RESOLUTION;
		float height = pageHeight / 72f * RESOLUTION;

		BufferedImage chartImage = chartRenderer.renderImage(element, false, (int)width, (int)height, (int)RESOLUTION);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(chartImage, "JPEG", baos);

		Image image = Image.getInstance(baos.toByteArray());
		image.scalePercent(72f/RESOLUTION*100f);
		image.setAlignment(Image.MIDDLE);
		

		doc.add(image);	
		
	}
}
