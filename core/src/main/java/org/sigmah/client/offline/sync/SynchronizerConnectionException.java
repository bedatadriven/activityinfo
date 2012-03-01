package org.sigmah.client.offline.sync;

public class SynchronizerConnectionException extends RuntimeException {

	public SynchronizerConnectionException(Throwable caught) {
		super(caught);
	}
	
	public SynchronizerConnectionException() {
	}

}
