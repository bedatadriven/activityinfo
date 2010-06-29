/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import org.sigmah.shared.report.model.ReportElement;

/**
 * Interface to classes which render {@link ReportElement}s into iText documents
 * 
 * @author Alex Bertram
 */
public interface ItextRenderer<ElementT extends ReportElement> {


  /**
   * 
   * Renders the ReportElement element into the iText Document object.
   * Where possible, subclasses should exclusively do the rendering with the 
   * Document object model, but specialized behaviors for PDF, RTFs, etc can 
   * be obtained by using the DocWriter interface.
   * 
   * @param writer The DocWriter (either {@link com.lowagie.text.pdf.PdfWriter PdfWriter} or 
   * {@link com.lowagie.text.rtf.RtfWriter2 RtfWriter2}
   * @param doc The document being built
   * @param element The ReportElement to be rendered
   * @throws DocumentException
   */
    void render(DocWriter writer, Document doc, ElementT element) throws DocumentException;

}
