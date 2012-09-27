package org.activityinfo.shared.command.handler;

import org.activityinfo.shared.command.ThrowingCommand;
import org.activityinfo.shared.command.result.VoidResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ThrowingCommandHandler implements CommandHandlerAsync<ThrowingCommand, VoidResult> {

	@Override
	public void execute(ThrowingCommand command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {
		context.getTransaction().executeSql("invalid sql");
	}

}
