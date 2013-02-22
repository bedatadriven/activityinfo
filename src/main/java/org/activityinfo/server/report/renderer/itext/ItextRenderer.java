
package org.activityinfo.server.report.renderer.itext;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.activityinfo.shared.report.model.ReportElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

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
