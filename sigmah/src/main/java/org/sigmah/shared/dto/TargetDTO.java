package org.sigmah.shared.dto;

import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.util.Format;

public class TargetDTO extends BaseModelData implements EntityDTO{

	private UserDatabaseDTO userDatabase;
	private List<TargetValueDTO> targetValues ;

	public final static String entityName = "Target";

	public TargetDTO() {
		super();
	}

	public TargetDTO(int id, String name) {
		super();

		setId(id);
		setName(name);
	}

	public int getId() {
		return (Integer) get("id");
	}

	public void setId(int id) {
		set("id", id);
	}

	public String getName() {
		return (String) get("name");
	}

	public void setName(String name) {
		set("name", name);
	}

	public void setDescription(String description) {
		set("description", description);
	}

	public String getDescription() {
		return (String) get("description");
	}

	public void setUserDatabase(UserDatabaseDTO database) {
		this.userDatabase = database;
	}

	public UserDatabaseDTO getUserDatabase() {
		return userDatabase;
	}

	public ProjectDTO getProject() {
		return get("project");
	}

	public void setProject(ProjectDTO value) {
		set("project", value);
	}

	public PartnerDTO getPartner() {
		return get("partner");
	}

	public void setPartner(PartnerDTO value) {
		set("partner", value);
	}

	public AdminEntityDTO getAdminEntity() {
		return get("adminEntity");
	}

	public void setAdminEntity(AdminEntityDTO value) {
		set("adminEntity", value);
	}

	public void setDate1(Date date1) {
		set("date1", date1);
	}

	public Date getDate1() {
		return (Date) get("date1");
	}

	public void setDate2(Date date2) {
		set("date2", date2);
	}

	public Date getDate2() {
		return (Date) get("date2");
	}
	
	public void setArea(String area){
		set("area", area);
	}
	
	public String getArea(){
		return get("area");
	}

	public List<TargetValueDTO> getTargetValues() {
		return targetValues;
	}

	public void setTargetValues(List<TargetValueDTO> targetValues) {
		this.targetValues = targetValues;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getEntityName() {
		return entityName;
	}
}
