package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

public class UserModel extends BaseModel implements DTO {
	
	public UserModel() {
		setAllowView(true);
        setAllowViewAll(false);
        setAllowEdit(false);
        setAllowEditAll(false);
        setAllowDesign(false);

	}
	
	public void setName(String value) { 
		set("name", value);
	}
	
	public String getName() {
		return get("name");
	}
	
	public void setEmail(String value) {
		set("email", value);
	}
	
	public String getEmail() {
		return get("email");
	}
	
	public void setAllowView(boolean value) {
		set("allowView", value);
	}
	
	public void setAllowDesign(boolean value) { 
		set("allowDesign", value);
	}
	
	public boolean getAllowDesign() {
		return (Boolean)get("allowDesign");
	}
	
	public boolean getAllowView() {
		return (Boolean)get("allowView");
	}

	public void setAllowViewAll(boolean value) {
		set("allowViewAll", value);
	}
	
	public boolean getAllowViewAll() {
		return (Boolean)get("allowViewAll");
	}
	
	public void setAllowEdit(boolean value) {
		set("allowEdit", value);
	}
	
	public boolean getAllowEdit() {
		return (Boolean)get("allowEdit");
	}

	public void setAllowEditAll(boolean value) {
		set("allowEditAll", value);
	}
	
	public boolean getAllowEditAll() {
		return (Boolean)get("allowEditAll");
	}
	
	public PartnerModel getPartner() { 
		return get("partner");
	}
	
	public void setPartner(PartnerModel value) {
		set("partner", value);
	}
}
