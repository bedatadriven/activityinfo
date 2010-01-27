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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.BaseMapDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetBaseMaps
 */
public class GetBaseMapsHandler implements CommandHandler<GetBaseMaps> {

    private BaseMapDAO baseMapDAO;

    @Inject
    public GetBaseMapsHandler(BaseMapDAO baseMapDAO) {
        this.baseMapDAO = baseMapDAO;
    }

    public CommandResult execute(GetBaseMaps cmd, User user) throws CommandException {
        return new BaseMapResult(baseMapDAO.getBaseMaps());
    }
}
