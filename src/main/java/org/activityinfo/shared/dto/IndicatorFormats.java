package org.activityinfo.shared.dto;

import com.google.gwt.i18n.client.NumberFormat;

public class IndicatorFormats {

	private static NumberFormat format = NumberFormat.getFormat("#,##0");
	
	public static NumberFormat get(IndicatorModel indicator) {
		if(indicator.getAggregation() == IndicatorModel.AGGREGATE_SUM || 
		   indicator.getAggregation() == IndicatorModel.AGGREGATE_SITE_COUNT ) {
			
			return format;
		}
		return format; 
	}
	
}
