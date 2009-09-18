package org.activityinfo.client.command.monitor;


public interface AsyncMonitor {

	/**
	 * Called just before a request is sent to the server
	 */
	void beforeRequest();
	
	/**
	 * Called when the async command completes either sucessfully or
     * with any CommandException except UnexpectedCommandException
	 */
	void onCompleted();
	
	/**
	 * Called when the async command fails due to network problems.
	 */
	void onConnectionProblem();
	
	/**
	 * Called before the async command is retried following a connection failure
	 * 
	 * @return true to allow the retry to continue, false to cancel 
	 */
	boolean onRetrying();
	
	/**
	 * Called when the async commands fails due to a server error (not 
	 * just a connection problem)
	 */
	void onServerError();
}
