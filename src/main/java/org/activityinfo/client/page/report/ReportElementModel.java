package org.activityinfo.client.page.report;

import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportElementModel extends BaseModelData{
		
	public ReportElementModel(ReportElement element) {
		setElementTitle(element.getTitle());
		setReportElement(element);
	}

	public void setId(int id){
		set("id", id);
	}
	
	public int getId(){
		return (Integer)get("id");
	}
	
	public void setEdited(boolean e){
		set("edited", e);
	}
	
	public boolean getEdited(){
		return (Boolean) get("edited");
	}
	
	public void setElementTitle(String elementTitle){
		set("elementTitle", elementTitle);
	}
	
	public String getElementTitle(){
		return get("elementTitle");
	}
	
	public void setReportElement(ReportElement reportElement){
		set("reportElement", reportElement);
	}
	
	public ReportElement getReportElement(){
		return (ReportElement)get("reportElement");
	}
	
}