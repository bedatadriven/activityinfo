package org.activityinfo.shared.report.model;

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

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.content.NullContent;

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

	@Override
	@XmlTransient
	public Set<Integer> getIndicators() {
		return Collections.emptySet();
	}

}
