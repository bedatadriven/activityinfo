package org.activityinfo.shared.report.model;

import org.activityinfo.shared.report.content.ReportContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Describes a document that contains a series of <code>ReportElement</code>s
 *  (e.g. Tables, Charts, etc} and is bound to a time frame.
 *
 * @see org.activityinfo.shared.report.model.ReportElement
 *
 * @author Alex Bertram
 */
public class Report extends ReportElement implements Serializable {


	private List<Parameter> parameters = new ArrayList<Parameter>();
	private List<ReportElement> elements = new ArrayList<ReportElement>();
	private String fileName;
    private int frequency;
    private int day = 1;

    private ReportContent content;
	
	public Report() {
		
	}

    /**
     * @return The list of report elements included in this report.
     */
	public List<ReportElement> getElements() {
		return elements;
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

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(Parameter parameter) {
		this.parameters.add(parameter);
	}

    /**
     *
     * @return Suggested filename for tbis report if it is saved to disk/downloaded. Does not contain
     * the file extension.
     */
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
     * @return The generated content of this report.
     */
    public ReportContent getContent() {
        return content;
    }

    public void setContent(ReportContent content) {
        this.content = content;
    }

    /**
     * Gets the frequency of this report
     *
     * @return the frequency of this report
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of this report
     *
     * @param frequency The frequency of this report
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }


    /**
     * Gets the day on which the report is to be mailed.
     *
     * If the subscription frequency is
     * <code>WEEKLY</code>, then this value refers to the
     * day of the week (0=Sunday, 6=Saturday). If the frequency is <code>MONTHLY</code> this refers to the
     * day of the month (1..28). A value of {@link ReportFrequency#LAST_DAY_OF_MONTH} (28)
     * indicats that the report will be mailed on the last day of the month, whether this the 28th, the 30th, or the 31st.
     *
     * @return the day on which the report is to be mailed.
     *
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day on which the report should be mailed.
     *
     * @param day For <code>WEEKLY</code> subscriptions, 0=Sunday, 6=Saturday. For <code>MONTHLY</code>,
     * subscriptions, 1=first day of month,2,3,4...28=<code>LAST_DAY_OF_MONTH</code>
     */
    public void setDay(int day) {
        this.day = day;
    }
}
