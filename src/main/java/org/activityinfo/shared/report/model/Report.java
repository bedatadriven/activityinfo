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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.report.content.ReportContent;

import com.google.common.collect.Sets;

/**
 * Describes a document that contains a series of <code>ReportElement</code>s
 * (e.g. Tables, Charts, etc} and is bound to a time frame.
 * 
 * @see org.activityinfo.shared.report.model.ReportElement
 */
@XmlRootElement(name = "report")
public class Report extends ReportElement<ReportContent> implements
    Serializable, CommandResult {

    private List<ReportElement> elements = new ArrayList<ReportElement>();

    private int id;

    private String fileName;
    private String description;

    private EmailDelivery frequency;
    private Integer day = null;

    public static final int LAST_DAY_OF_MONTH = 28;

    public Report() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The list of report elements included in this report.
     */
    @XmlElements({
        @XmlElement(name = "pivotTable", type = PivotTableReportElement.class),
        @XmlElement(name = "pivotChart", type = PivotChartReportElement.class),
        @XmlElement(name = "table", type = TableElement.class),
        @XmlElement(name = "map", type = MapReportElement.class),
        @XmlElement(name = "text", type = TextReportElement.class),
        @XmlElement(name = "image", type = ImageReportElement.class)
    })
    @XmlElementWrapper(name = "elements")
    public List<ReportElement> getElements() {
        return elements;
    }

    public <T extends ReportElement> T getElement(int index) {
        return (T) elements.get(index);
    }

    public void setElements(List<ReportElement> elements) {
        this.elements = elements;
    }

    /**
     * Adds a ReportElement to this report's definition.
     * 
     * @param element
     *            A <code>ReportElement</code>
     */
    public void addElement(ReportElement element) {
        elements.add(element);
    }

    /**
     * 
     * @return Suggested filename for tbis report if it is saved to
     *         disk/downloaded. Does not contain the file extension.
     */
    @XmlElement
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the suggested filename for this report in the event it is saved to
     * disk/downloaded. Should not contain the extension
     * 
     * @param fileName
     *            The suggested filename, without extension
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    @XmlTransient
    public Set<Integer> getIndicators() {
        Set<Integer> ids = Sets.newHashSet();
        for (ReportElement element : getElements()) {
            ids.addAll(element.getIndicators());
        }
        return ids;
    }

    @Override
    public String toString() {
        return "Report [elements="
            + com.google.common.base.Joiner.on('\n').join(elements) + elements
            + "]";
    }

}
