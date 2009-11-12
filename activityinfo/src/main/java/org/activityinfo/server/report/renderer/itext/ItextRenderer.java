package org.activityinfo.server.report.renderer.itext;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * @author Alex Bertram
 */
public interface ItextRenderer<ElementT> {

    public void render(DocWriter writer, ElementT element, Document doc) throws DocumentException;

}
