package org.activityinfo.shared.exception;

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

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

public class CommandException extends RuntimeException implements CommandResult {
    private static final long serialVersionUID = -127571117379851453L;

    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable e) {
        super(e);
    }

    public CommandException(String message, Throwable e) {
        super(message, e);
    }

    public CommandException(Command<?> command, Throwable e) {
        super((command.getClass().getName().substring(command.getClass().getName().lastIndexOf(".") + 1))
            + " - " + e.getMessage(), e);
        this.setStackTrace(new StackTraceElement[] {});
    }
}
