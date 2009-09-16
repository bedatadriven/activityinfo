package org.activityinfo.shared.report.model;

import com.extjs.gxt.ui.client.data.BaseModel;

import java.io.Serializable;

/**
 * 
 * ReportElement is the base class for all report elements and the 
 * report container itself.
 * 
 * 
 * 
 * @author Alex Bertram
 *
 */
public abstract class ReportElement extends BaseModel {


	private ParameterizedFilter filter = new ParameterizedFilter();
	
	public ReportElement() {
		
	}

	/**
	 * The filter that will be applied to this report. 
	 * Note that elements inherit the report's global filter,
	 * as well as any other filter specified by the callers
	 * at runtime.
	 * 
	 * @return
	 */
	public ParameterizedFilter getFilter() {
		return filter;
	}

	public void setFilter(ParameterizedFilter filter) {
		this.filter = filter;
	}

	/**
	 * The full title of the report element
	 *  
	 * @return The full title of the report element
	 */
	public String getTitle() {
		return get("title");
	}

	public void setTitle(String title) {
		set("title", title);
	}

	/**
	 * A shorter form of the title used to name worksheet tabs 
	 * (e.g. something other than Sheet1, Sheet2, Sheet3 at the 
	 * bottom of Excel)
	 * 
	 * @return
	 */
	public String getSheetTitle() {
		return get("sheetTitle");
	}

	public void setSheetTitle(String sheetTitle) {
		set("sheetTitle", sheetTitle);
	}

}
