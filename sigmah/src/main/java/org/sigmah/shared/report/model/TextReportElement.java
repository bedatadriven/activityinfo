package org.sigmah.shared.report.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.sigmah.shared.report.content.NullContent;

/**
 * Report element containing static text
 *
 */
public class TextReportElement extends ReportElement<NullContent> {
	
	private String text;
	
	public TextReportElement() {
		
	}
	
	public TextReportElement(String paragraph) {
		this.text = paragraph;
	}

	/**
	 * @return the plain text to be included in the report. 
	 */
	@XmlElement(name="p")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
