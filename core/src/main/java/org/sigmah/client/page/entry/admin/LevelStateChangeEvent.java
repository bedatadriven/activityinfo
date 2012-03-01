package org.sigmah.client.page.entry.admin;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Signals that an AdminLevel's enabled state has changed 
 * based on the current selection in {@link AdminFieldSetPresenter}
 */
public class LevelStateChangeEvent extends BaseEvent {

	public static final EventType TYPE = new EventType();
	
	private int levelId;
	private boolean enabled;
	
	public LevelStateChangeEvent(int levelId, boolean enabled) {
		super(TYPE);
		this.levelId = levelId;
		this.enabled = enabled;
	}

	public int getLevelId() {
		return levelId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + levelId;
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
		LevelStateChangeEvent other = (LevelStateChangeEvent) obj;
		if (enabled != other.enabled) {
			return false;
		}
		if (levelId != other.levelId) {
			return false;
		}
		return true;
	}
	
	
}
