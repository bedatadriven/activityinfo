package org.sigmah.shared.command;

import java.util.Map;

import org.sigmah.shared.command.result.VoidResult;

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateSite other = (UpdateSite) obj;
		if (changes == null) {
			if (other.changes != null)
				return false;
		} else if (!changes.equals(other.changes))
			return false;
		if (siteId != other.siteId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UpdateSite{ id=" + siteId + ", changes=" + changes.getTransientMap().toString() + "}";
	} 
	
	
}
