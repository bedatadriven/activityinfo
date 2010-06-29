package org.sigmah.shared.command.result;

public interface SingleResult<T> extends CommandResult {

	public T getResult();

}
