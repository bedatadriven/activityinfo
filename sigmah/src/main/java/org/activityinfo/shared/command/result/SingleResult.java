package org.activityinfo.shared.command.result;

public interface SingleResult<T> extends CommandResult {

	public T getResult();

}
