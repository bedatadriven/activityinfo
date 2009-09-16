package org.activityinfo.shared.report.model;

import org.activityinfo.shared.report.content.ReportContent;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class Report extends ReportElement implements Serializable {


	private List<Parameter> parameters = new ArrayList<Parameter>();
	private List<ReportElement> elements = new ArrayList<ReportElement>();
	private String fileName;

    private ReportContent content;
	
	public Report() {
		
	}

	public List<ReportElement> getElements() {
		return elements;
	}

	public void setElements(List<ReportElement> elements) {
		this.elements = elements;
	}

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    public ReportContent getContent() {
        return content;
    }

    public void setContent(ReportContent content) {
        this.content = content;
    }
}
