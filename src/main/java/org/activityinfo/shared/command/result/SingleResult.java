package org.activityinfo.shared.command.result;

import org.activityinfo.shared.command.result.CommandResult;

public interface SingleResult<T> extends CommandResult {

	public T getResult();

}
