package org.activityinfo.client.dispatch.remote;

/**
 * Handles the case when the client is out of date with the server and can no longer
 *  communicate via RPC.
 */
public interface IncompatibleRemoteHandler {
	void handle();
}
