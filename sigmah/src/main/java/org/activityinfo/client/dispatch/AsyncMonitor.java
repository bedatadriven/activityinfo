package org.activityinfo.client.dispatch;


/**
 * The <code>AsyncMonitor</code> provides a complement to the AsyncCallback
 * interface, allowing a division of responibility between application logic,
 * which is handled by classes implementing AsyncCallback, and which handle
 * command success or "expected" <code>CommandException</code> and the AsyncMonitor
 * which provides feedback to the user regarding progress, completion, connection
 * problems, general surver failures, and <code>UnexpectedCommandException</code>s
 */
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
