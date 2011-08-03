package org.sigmah.client.page;

import org.sigmah.client.dispatch.AsyncMonitor;

import com.google.gwt.user.client.TakesValue;

/*
 * The view always has a primary domain object to display. The view receives calls 
 * from the Presenter (the Presenter having an instance of the View), and the View 
 * throws events the Presenter subscribes to. The View does not have an instance of
 * the Presenter. The View only has 'dumb' methods: the Presenter acts as a proxy 
 * between the model and the view.
 */
public interface View<T> extends TakesValue<T> {
	/*
	 * Presenters have an async process of fetching data. Only after the data has been
	 * fetched successfully and the data set on the View, the Presenter calls this method
	 */
	public void initialize();
	
	/*
	 * The UI displaying loading, network status (retry/error/complete)
	 * Usually some standard Async monitor UI view, such as NullAsyncMonitor,
	 * MaskingAsyncMonitor etc
	 * 
	 */
	public AsyncMonitor getAsyncMonitor();
}
