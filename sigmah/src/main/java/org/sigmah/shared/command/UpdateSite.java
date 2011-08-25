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
	
	
}
