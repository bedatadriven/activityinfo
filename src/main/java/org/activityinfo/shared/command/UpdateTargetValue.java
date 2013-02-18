package org.activityinfo.shared.command;

import java.util.Map;

import org.activityinfo.shared.command.result.VoidResult;

import com.extjs.gxt.ui.client.data.RpcMap;

public class UpdateTargetValue implements MutatingCommand<VoidResult> {

	private int targetId;
	private int indicatorId;
	private RpcMap changes;

	public UpdateTargetValue(){
		
	}
	
	public UpdateTargetValue(int targetId, int indicatorId, Map<String, Object> changes) {
        this.targetId = targetId;
        this.indicatorId = indicatorId;
		this.changes = new RpcMap();
        this.changes.putAll(changes);
    }
	
	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public int getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}

	public RpcMap getChanges() {
		return changes;
	}

	public void setChanges(RpcMap changes) {
		this.changes = changes;
	}

}
