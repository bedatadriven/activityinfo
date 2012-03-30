/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.report.content.ReportContent;

import com.google.common.collect.Sets;

/**
 * Describes a document that contains a series of <code>ReportElement</code>s
 *  (e.g. Tables, Charts, etc} and is bound to a time frame.
 *
 * @see org.sigmah.shared.report.model.ReportElement
 */
@XmlRootElement(name="report")
public class Report extends ReportElement<ReportContent> implements Serializable, CommandResult {


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
            @XmlElement(name="pivotTable", type=PivotTableReportElement.class),
            @XmlElement(name="pivotChart", type=PivotChartReportElement.class),
            @XmlElement(name="table", type=TableElement.class),
            @XmlElement(name="map", type=MapReportElement.class),
            @XmlElement(name="text", type=TextReportElement.class),
            @XmlElement(name="image", type=ImageReportElement.class)
    })
    @XmlElementWrapper(name="elements")
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
     * @param element A <code>ReportElement</code>
     */
	public void addElement(ReportElement element) {
		elements.add(element);
	}

    /**
     *
     * @return Suggested filename for tbis report if it is saved to disk/downloaded. Does not contain
     * the file extension.
     */
    @XmlElement
	public String getFileName() {
		return fileName;
	}

    /**
     * Sets the suggested filename for this report in the event it is saved to disk/downloaded.
     * Should not
     * contain the extension
     *
     * @param fileName The suggested filename, without extension
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    /**
     * Gets the frequency of this report
     *
     * @return the frequency of this report
     */
    @XmlAttribute(name="frequency")
    public EmailDelivery getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of this report
     *
     * @param frequency The frequency of this report
     */
    public void setFrequency(EmailDelivery frequency) {
        this.frequency = frequency;
    }


    /**
     * Gets the day on which the report is to be mailed.
     *
     * If the subscription frequency is
     * <code>WEEKLY</code>, then this value refers to the
     * day of the week (0=Sunday, 6=Saturday). If the frequency is <code>MONTHLY</code> this refers to the
     * day of the month (1..28). A value of {@link Report#LAST_DAY_OF_MONTH} (28)
     * indicats that the report will be mailed on the last day of the month, whether this the 28th, the 30th, or the 31st.
     *
     * @return the day on which the report is to be mailed.
     *
     */
    @XmlAttribute
    public Integer getDay() {
        return day;
    }

    /**
     * Sets the day on which the report should be mailed.
     *
     * @param day For <code>WEEKLY</code> subscriptions, 0=Sunday, 6=Saturday. For <code>MONTHLY</code>,
     * subscriptions, 1=first day of month,2,3,4...28=<code>LAST_DAY_OF_MONTH</code>
     */
    public void setDay(Integer day) {
        this.day = day;
    }

	@Override
	@XmlTransient
	public Set<Integer> getIndicators() {
		Set<Integer> ids = Sets.newHashSet();
		for(ReportElement element : getElements()) {
			ids.addAll(element.getIndicators());
		}
		return ids;
	}
}
