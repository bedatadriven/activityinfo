package org.activityinfo.client.page.entry.admin;

import org.activityinfo.shared.util.mapping.Extents;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Signals that the geographic bounds of the selection in the 
 * AdminFieldSetPresenter have changed. 
 */
public class BoundsChangedEvent extends BaseEvent {

	public static final EventType TYPE = new EventType();
	
	private Extents bounds;
	private String name;
	
	public BoundsChangedEvent(Extents bounds, String name) {
		super(TYPE);
		this.bounds = bounds;
		this.name = name;
	}

	public Extents getBounds() {
		return bounds;
	}

	public void setBounds(Extents bounds) {
		this.bounds = bounds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bounds == null) ? 0 : bounds.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BoundsChangedEvent other = (BoundsChangedEvent) obj;
		if (bounds == null) {
			if (other.bounds != null) {
				return false;
			}
		} else if (!bounds.equals(other.bounds)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BoundsChangedEvent [bounds=" + bounds + ", name=" + name + "]";
	}
}
