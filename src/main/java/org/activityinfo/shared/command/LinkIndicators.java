package org.activityinfo.shared.command;

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

import org.activityinfo.shared.command.result.VoidResult;

public class LinkIndicators implements Command<VoidResult> {
	private boolean link;
	private int sourceIndicatorId;
	private int destIndicatorId;
	
	public LinkIndicators() {
		super();
	}

	public boolean isLink() {
		return link;
	}

	public void setLink(boolean link) {
		this.link = link;
	}

	public int getSourceIndicatorId() {
		return sourceIndicatorId;
	}

	public void setSourceIndicatorId(int sourceIndicatorId) {
		this.sourceIndicatorId = sourceIndicatorId;
	}

	public int getDestIndicatorId() {
		return destIndicatorId;
	}

	public void setDestIndicatorId(int destIndicatorId) {
		this.destIndicatorId = destIndicatorId;
	}
	
	
}
