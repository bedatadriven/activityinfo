package org.sigmah.shared.command;

import java.util.Map;

import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.RpcMap;

public class UpdateSite implements MutatingCommand<VoidResult> {

	private int siteId;
	private RpcMap changes;

	public UpdateSite() {
		
	}
	
	public UpdateSite(int siteId, RpcMap changes) {
		this.siteId = siteId;
		this.changes = changes;
	}
	
	public UpdateSite(int siteId, Map<String,Object> changes) {
		this.siteId = siteId;
		this.changes = new RpcMap();
		this.changes.putAll(changes);
	}
	
	public UpdateSite(SiteDTO original, SiteDTO updated) {
		assert original.getId() == updated.getId();
		changes = new RpcMap();
		for(String property : updated.getProperties().keySet()) {
			Object newValue = updated.get(property);
			if(isChanged(original.get(property), newValue)) {
				if(property.equals("partner")) {
					changes.put("partnerId", newValue == null ? null : ((PartnerDTO)newValue).getId());
				} else if(property.equals("project")) {
					changes.put("projectId", newValue == null ? null : ((ProjectDTO)newValue).getId());
				} else if(propertyCanBeModified(property)) {
					changes.put(property, newValue);
				}
			}
		}
	}
	
	private boolean propertyCanBeModified(String property) {
		return ! (property.equals("activityId") ||
				  property.startsWith(AdminLevelDTO.PROPERTY_PREFIX));
	}

	private boolean isChanged(Object a, Object b) {
		if(a == null) {
			return b != null;
		} else {
			return !a.equals(b);
		}
	}
	
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public RpcMap getChanges() {
		return changes;
	}
	public void setChanges(RpcMap changes) {
		this.changes = changes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changes == null) ? 0 : changes.hashCode());
		result = prime * result + siteId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UpdateSite other = (UpdateSite) obj;
		if (changes == null) {
			if (other.changes != null) {
				return false;
			}
		} else if (!changes.equals(other.changes)) {
			return false;
		}
		if (siteId != other.siteId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UpdateSite{ id=" + siteId + ", changes=" + changes.getTransientMap().toString() + "}";
	} 	
}
