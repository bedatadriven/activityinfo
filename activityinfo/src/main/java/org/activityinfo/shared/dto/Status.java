package org.activityinfo.shared.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.activityinfo.shared.i18n.UIConstants;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.core.client.GWT;

public class Status extends BaseModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1886280012043940632L;

	public Status(int value, String label) {
		set("value", value);
		set("label", label);
	}
	
	public static List<Status> getStatusValues() {
		UIConstants locale = (UIConstants) GWT.create(UIConstants.class);
		
		List<Status> list = new ArrayList<Status>();
		list.add(new Status(-2, locale.planned()));
		list.add(new Status(-1, locale.inProgress()));
		list.add(new Status(0, locale.cancelled()));
		list.add(new Status(1, locale.complete()));	
		return list;
	}
	
	public static Map<Integer, String> getStatusMap() { 
		UIConstants locale = (UIConstants) GWT.create(UIConstants.class);
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		map.put(-2, locale.planned());
		map.put(-1, locale.inProgress());
		map.put(0, locale.cancelled());
		map.put(1, locale.complete());	
		return map;
	}
	
	public int getValue() { 
		return (Integer)get("value");
	}
	
	public boolean IsStarted() {
		return getValue() > -1;
	}
	
	public boolean IsFinished() { 
		return getValue() == 1;
	}
}

