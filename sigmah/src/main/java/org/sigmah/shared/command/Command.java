/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CommandResult;

import java.io.Serializable;

/**
 * Marker interface for RPC commands.
 * <p/>
 * Rather than defining a method in the {@link org.sigmah.shared.command.RemoteCommandService}
 * interface for each server-side operation, we define a single
 * {@link org.sigmah.shared.command.RemoteCommandService#execute(String, java.util.List)} method
 * which accepts objects that implement this marker interface.
 * <p/>
 * On the server, each Command class has a corresponding {@link org.sigmah.shared.command.handler.CommandHandler}
 * class which carries out the operation.
 * <p/>
 * This approach is a bit of a riff on Aspect Oriented Programming (AOP),
 * allowing us to handle all manners of cross-cutting
 * concerns, such as batching, error handling, retrying, and failure on the client side (See
 * {@link org.sigmah.client.dispatch.remote.RemoteDispatcher}) and transactions, logging, and authentication
 * on the server-side. (See {@link org.sigmah.server.endpoint.gwtrpc.CommandServlet}).
 *
 * @author Alex Bertram
 * @param <T> The type of return type expected as a result
 */
public interface Command<T extends CommandResult> extends Serializable {


}
