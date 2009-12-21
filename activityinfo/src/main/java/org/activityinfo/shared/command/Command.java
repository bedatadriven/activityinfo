package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CommandResult;

import java.io.Serializable;

/**
 * Marker interface for RPC commands.
 *
 * Rather than defining a method in the {@link org.activityinfo.shared.command.RemoteCommandService}
 * interface for each server-side operation, we define a single
 * {@link org.activityinfo.shared.command.RemoteCommandService#execute(String, java.util.List)} method
 * which accepts objects that implement this marker interface.
 *
 * On the server, each Command class has a corresponding {@link org.activityinfo.server.command.handler.CommandHandler}
 * class which carries out the operation.
 * 
 * This approach is a bit of a riff on Aspect Oriented Programming (AOP),
 * allowing us to handle all manners of cross-cutting
 * concerns, such as batching, error handling, retrying, and failure on the client side (See
 * {@link org.activityinfo.client.command.CommandServiceImpl}) and transactions, logging, and authentication
 * on the server-side. (See {@link org.activityinfo.server.endpoint.gwtrpc.CommandServlet}).
 *
 * @author Alex Bertram
 *
 * @param <T> The type of return type expected as a result
 */
public interface Command<T extends CommandResult> extends Serializable {


		
}
