package org.sigmah.shared.report.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.sigmah.shared.report.content.NullContent;

/**
 * 
 * Defines an external, static image to be included in the report
 * 
 */
public class ImageElement extends ReportElement<NullContent>{
	
	private String url;

	
	/**
	 * 
	 * @return the URL of the image to include in the report
	 */
	@XmlElement
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
