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

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class GenerateElementHandler implements CommandHandler<GenerateElement> {


    private final ReportGenerator generator;

    @Inject
    public GenerateElementHandler(ReportGenerator generator) {
        this.generator = generator;
    }

    public CommandResult execute(GenerateElement cmd, User user) throws CommandException {

        HashMap<String, Object> params = new HashMap<String, Object>();

        return generator.generateElement(user, cmd.getElement(), null, params);
       
    }
}
