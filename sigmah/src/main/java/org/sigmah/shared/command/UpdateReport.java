package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.ReportElement;

public class UpdateReport implements Command<VoidResult>{

	private int id;
	private String title;
	private List<ReportElement> elements;
	
	public UpdateReport(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<ReportElement> getElement() {
		return elements;
	}

	public void setElement(List<ReportElement> elements) {
		this.elements = elements;
	}
	
}
