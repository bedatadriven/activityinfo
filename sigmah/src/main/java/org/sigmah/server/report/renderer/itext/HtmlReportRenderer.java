/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;

import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.ChartRendererJC;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import antlr.CharBuffer;

import com.google.inject.Inject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Jpeg;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.HtmlWriter;


/**
 * iText ReportRenderer targeting HTML output
 *
 */
public class HtmlReportRenderer extends ItextReportRenderer {

	private final ImageStorageProvider imageStorageProvider;
	
    @Inject
    public HtmlReportRenderer(@MapIconPath String mapIconPath, ImageStorageProvider imageStorageProvider) {
    	super(new ItextHtmlChartRenderer(new ChartRendererJC(), imageStorageProvider), mapIconPath);
    	this.imageStorageProvider = imageStorageProvider;
    }

    @Override
    protected DocWriter createWriter(Document document, OutputStream os) throws DocumentException {
    	return new MyHtmlWriter(document, os);
    }
   

    @Override
    public String getMimeType() {
        return "text/html";
    }

    @Override
    public String getFileSuffix() {
        return ".html";
    }
    
    public void render(ReportElement element, final Writer writer) throws IOException {
    	// The HtmlWriter encodes everythings as ISO-8859-1
    	// so we can be safely naive here about encoding
    	final Charset charset = Charset.forName("ISO-8859-1");
    	render(element, new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				writer.append((char)b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				writer.append(new String(b,off,len,charset));
			}
		});
    }    
    
    @Override
	protected void renderFooter(Document document) {
    	// no footer for HTML
	}



	class MyHtmlWriter extends HtmlWriter {

		public MyHtmlWriter(Document doc, OutputStream os) {
			super(doc, os);
		}

		@Override
		protected void write(Element element, int indent) throws IOException {
			if(element instanceof Image) {
				Image image = (Image)element;		
				writeStart(HtmlTags.DIV);
				os.write(GT);

				if(image.getUrl() != null) {
					writeImage(image, image.getUrl().toString());
					
				} else if(image instanceof Jpeg) {
					ImageStorage storage = imageStorageProvider.getImageUrl(".jpg");
					storage.getOutputStream().write(image.getRawData());
					storage.getOutputStream().close();
					writeImage(image, storage.getUrl());
				}
				writeEnd(HtmlTags.DIV);
			} else {
				super.write(element, indent);
			}
		}
		
		// copied from HtmlWriter
		void writeImage(Image image, String url) throws IOException {
			   writeImageAtUrl(url, image.getAlignment(), image.getScaledWidth(), image.getScaledHeight());
		}

		void writeImageAtUrl(String url, int alignment, float scaledWidth, float scaledHeight)
				throws IOException {
			writeStart(HtmlTags.IMAGE);
			write(HtmlTags.URL, url);
			if ((alignment & Image.RIGHT) > 0) {
				write(HtmlTags.ALIGN, HtmlTags.ALIGN_RIGHT);
			}
			else if ((alignment & Image.MIDDLE) > 0) {
				write(HtmlTags.ALIGN, HtmlTags.ALIGN_MIDDLE);
			}
			else {
				write(HtmlTags.ALIGN, HtmlTags.ALIGN_LEFT);
			}

			write(HtmlTags.PLAINWIDTH, String.valueOf(scaledWidth));
			write(HtmlTags.PLAINHEIGHT, String.valueOf(scaledHeight));
			writeMarkupAttributes(markup);
			writeEnd();
		}
    }
    
    
    
}
