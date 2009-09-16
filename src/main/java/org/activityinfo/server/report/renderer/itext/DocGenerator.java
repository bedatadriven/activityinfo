package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.generator.ContentGenerator;
import org.activityinfo.shared.report.model.ReportElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

public interface DocGenerator<WriterT extends DocWriter, T extends ReportElement> {

	public void addContent(WriterT writer, Document document, T element ) throws DocumentException;
	
}
