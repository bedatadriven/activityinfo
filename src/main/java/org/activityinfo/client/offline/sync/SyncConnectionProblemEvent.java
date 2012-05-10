package org.activityinfo.client.offline.sync;

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
