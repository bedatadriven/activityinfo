package org.activityinfo.shared.command.handler;

import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExecutionContext {

	AuthenticatedUser getUser();

	boolean isLocal();
	
	/**
	 * 
	 * @return the SqlTransaction associated with the execution of the current command. 
	 * Each command is executed within its own transaction, but any nested command 
	 * will share it's parent transaction.
	 */
	SqlTransaction getTransaction();
	
	
	/**
	 * Executes a nested command
	 * 
	 * @param command the command to execute 
	 * @param callback callback on the command's completion
	 */
	<C extends Command<R>, R extends CommandResult> void execute(C command, AsyncCallback<R> callback);

}
