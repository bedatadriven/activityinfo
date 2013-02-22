package org.activityinfo.client.page.entry.admin;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.shared.dto.AdminEntityDTO;

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
