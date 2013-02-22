package org.activityinfo.shared.command;

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

import java.io.Serializable;

import org.activityinfo.shared.command.result.CommandResult;

/**
 * Marker interface for RPC commands.
 * <p/>
 * Rather than defining a method in the
 * {@link org.activityinfo.shared.command.RemoteCommandService} interface for
 * each server-side operation, we define a single
 * {@link org.activityinfo.shared.command.RemoteCommandService#execute(String, java.util.List)}
 * method which accepts objects that implement this marker interface.
 * <p/>
 * On the server, each Command class has a corresponding
 * {@link org.activityinfo.server.command.handler.CommandHandler} class which
 * carries out the operation.
 * <p/>
 * This approach is a bit of a riff on Aspect Oriented Programming (AOP),
 * allowing us to handle all manners of cross-cutting concerns, such as
 * batching, error handling, retrying, and failure on the client side (See
 * {@link org.activityinfo.client.dispatch.remote.MergingDispatcher}) and
 * transactions, logging, and authentication on the server-side. (See
 * {@link org.activityinfo.server.endpoint.gwtrpc.CommandServlet}).
 * 
 * @author Alex Bertram
 * @param <T>
 *            The type of return type expected as a result
 */
public interface Command<T extends CommandResult> extends Serializable {

}
