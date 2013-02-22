package org.activityinfo.client.local.sync;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.local.command.CommandQueue;
import org.activityinfo.client.local.command.CommandQueue.QueueEntry;
import org.activityinfo.shared.command.result.CommandResult;

import com.bedatadriven.rebar.async.Async;
import com.bedatadriven.rebar.async.AsyncCommand;
import com.bedatadriven.rebar.async.AsyncFunction;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Sends updates to the remote database. 
 * 
 */
@Singleton
public class UpdateSynchronizer implements AsyncCommand {

	private CommandQueue commandQueue;
	private Dispatcher dispatcher;
	
	@Inject
	public UpdateSynchronizer(CommandQueue commandQueue, SynchronizerDispatcher dispatcher) {
		super();
		this.commandQueue = commandQueue;
		this.dispatcher = dispatcher;
	}
	
	private AsyncFunction<QueueEntry, CommandResult> dispatch() {
		return new AsyncFunction<CommandQueue.QueueEntry, CommandResult>() {
			
			@Override
			protected void doApply(QueueEntry argument, AsyncCallback<CommandResult> callback) {
				dispatcher.execute(argument.getCommand(),  callback);
			}
		};
	}
	
	public void execute(AsyncCallback<Void> callback) {
		commandQueue.get().map(
				Async.sequence(dispatch(), 
							   commandQueue.remove()))
			.discardResult()
			.apply(null, callback);	
	}
	
}
