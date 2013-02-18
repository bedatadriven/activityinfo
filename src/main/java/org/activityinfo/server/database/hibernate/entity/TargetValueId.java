package org.activityinfo.server.database.hibernate.entity;

import javax.persistence.Column;

public class TargetValueId  implements java.io.Serializable {

	private int targetId;
	private int indicatorId;
	
	public TargetValueId (){
		
	}
	
	public TargetValueId (int targetId, int indicatorId){
		this.targetId = targetId;
		this.indicatorId = indicatorId;
	}
	
	@Column(name = "targetId", nullable = false)
	public int getTargetId() {
		return targetId;
	}
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	
	@Column(name = "indicatorId", nullable = false)
	public int getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}
	
	
}
