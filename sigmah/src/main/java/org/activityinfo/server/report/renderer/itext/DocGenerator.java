package org.activityinfo.server.report.renderer.itext;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import org.activityinfo.shared.report.model.ReportElement;

public interface DocGenerator<WriterT extends DocWriter, T extends ReportElement> {

	public void addContent(WriterT writer, Document document, T element ) throws DocumentException;
	
}
