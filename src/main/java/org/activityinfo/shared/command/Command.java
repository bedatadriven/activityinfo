package org.activityinfo.shared.command;

import com.google.gwt.user.client.rpc.IsSerializable;

import org.activityinfo.shared.command.result.CommandResult;

/**
 * Marker interface for RPC commands
 *
 * @author Alex Bertram
 *
 * @param <T> The type of return type expected as a result
 */
public interface Command<T extends CommandResult> extends IsSerializable {


		
}
