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

import com.google.inject.Inject;
import org.sigmah.server.dao.BaseMapDAO;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetBaseMaps
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
