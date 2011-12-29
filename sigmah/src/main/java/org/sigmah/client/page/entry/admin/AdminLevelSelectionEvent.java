package org.sigmah.client.page.entry.admin;

import org.sigmah.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * An event that signals that the current selection within
 * {@link AdminFieldSetPresenter} has changed.
 */
public class AdminLevelSelectionEvent extends BaseEvent {
	
	public static final EventType TYPE = new EventType();
	
	private int levelId;
	private AdminEntityDTO value;
	
	public AdminLevelSelectionEvent(int levelId, AdminEntityDTO value) {
		super(TYPE);
		this.levelId = levelId;
		this.value = value;
	}

	public int getLevelId() {
		return levelId;
	}

	public AdminEntityDTO getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + levelId;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		AdminLevelSelectionEvent other = (AdminLevelSelectionEvent) obj;
		if (levelId != other.levelId) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AdminSelectionEvent [levelId=" + levelId + ", value=" + value
				+ "]";
	}
}
