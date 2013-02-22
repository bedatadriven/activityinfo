package org.activityinfo.client.local.sync;

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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class SyncConnectionProblemEvent extends BaseEvent {

    public static final EventType TYPE = new EventType();
    
    private int attempt;
    private int retryDelay;
	
	public SyncConnectionProblemEvent(int attempt, int retryDelay) {
		super(TYPE);
		this.attempt = attempt;
		this.retryDelay = retryDelay;
	}

	/**
	 * 
	 * @return the number of retries
	 */
	public int getAttempt() {
		return attempt;
	}

	/**
	 * 
	 * @return delay until the next retry
	 */
	public int getRetryDelay() {
		return retryDelay;
	}
}
