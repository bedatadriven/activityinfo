package org.sigmah.shared.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class Collector<T> implements AsyncCallback<T> {

	private T result = null;
	private boolean callbackCalled = false;
	
	@Override
	public void onFailure(Throwable caught) {
		if(caught instanceof RuntimeException) {
			throw (RuntimeException)caught;
		} else {
			throw new RuntimeException(caught);
		}
	}

	@Override
	public void onSuccess(T result) {
		if(callbackCalled) {
			throw new IllegalStateException("Callback called a second time");
		}
		this.callbackCalled = true;
		this.result = result;
	}
	
	public T getResult() {
		if(!callbackCalled) {
			throw new IllegalStateException("Callback was not called");
		}
		return result;
	}
	
	public static <T> Collector<T> newCollector() {
		return new Collector<T>();
	}

}
