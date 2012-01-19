package org.sigmah.shared.report.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportSubscriber extends BaseModelData{

	public ReportSubscriber(){
		
	}
	
	public ReportSubscriber(String email) {
		setEmail(email);
		setSubscribed(true);
	}

	public void setEmail(String email) {
		set("email", email);
	}

	public String getEmail() {
		return (String) get("email");
	}
	
	public void setSubscribed(boolean subscribed){
		set("subscribed", subscribed);
	}
	
	public boolean isSubscribed(){
		return get("subscribed");
	}
}
