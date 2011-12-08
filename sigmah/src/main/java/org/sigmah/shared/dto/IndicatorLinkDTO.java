package org.sigmah.shared.dto;

import java.util.HashMap;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class IndicatorLinkDTO extends BaseModelData implements EntityDTO{

	public final static String entityName = "IndicatorLink";
	
	
	public void setSourceIndicator(int id){
		set("sourceIndicatorId" , id);
	}
	
	public int getSourceIndicator(){
		return (Integer)get("sourceIndicatorId");
	}
	
	public void setDestinationIndicator(HashMap<Integer, String> destinationIndicators){
		set("destinationIndicators" , destinationIndicators);
	}
	
	public HashMap<Integer, String> getDestinationIndicator(){
		return (HashMap<Integer, String>) get("destinationIndicators");
	}
	
	@Override
	public int getId() {
		return 0;
	}

	@Override
	public String getEntityName() {
		
		return entityName;
	}

	@Override
	public String getName() {
		return entityName;
	}

}
