package org.activityinfo.shared.report.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.content.TableContent;

public class StaticReportElement extends ReportElement < TableContent > implements Serializable {
	
	private String text;
	private String img;
	
	@XmlElement
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@XmlElement
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@Override
	@XmlTransient
	public Set<Integer> getIndicators() {
		return Collections.emptySet();
	}
}
