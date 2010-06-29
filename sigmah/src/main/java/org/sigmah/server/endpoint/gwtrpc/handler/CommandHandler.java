/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import org.sigmah.server.domain.User;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

/**
 * Command executors are the server half of {@link Command}s defined in the
 * client package. Each {@link Command} has its corresponding executor which
 * is responsible for carrying out the command on the server.
 *
 * @author Alex Bertram
 */
public interface CommandHandler<CommandT extends Command> {

    /*
      * TODO: is there anyway the return type can be automatically parameratized
      * with the type parameter of CommandT ? (and without adding a second type
      * parameter to CommandHandler
      */


    /**
     * Execute a command received from the client
     *
     * @param <T> Result type
     * @param cmd Command received from the server
     * @return The result of command if successful. If the command is not successful, an exception should be thrown.
     * @throws org.sigmah.shared.exception.CommandException
     *
     */
    public CommandResult execute(CommandT cmd, User user) throws CommandException;


}
