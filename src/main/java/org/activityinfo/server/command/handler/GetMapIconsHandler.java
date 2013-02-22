

package org.activityinfo.server.command.handler;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.shared.command.GetMapIcons;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.MapIconResult;
import org.activityinfo.shared.dto.MapIconDTO;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetMapIcons
 */
public class GetMapIconsHandler implements CommandHandler<GetMapIcons> {

    private String mapIconPath;

    @Inject
    public GetMapIconsHandler(@MapIconPath String mapIconPath) {
        this.mapIconPath = mapIconPath;
    }

    public CommandResult execute(GetMapIcons cmd, User user) throws CommandException {

        File iconFolder = new File(mapIconPath);

        List<MapIconDTO> list = new ArrayList<MapIconDTO>();

        for (String file : iconFolder.list()) {
            if (file.endsWith(".png")) {
                MapIconDTO icon = new MapIconDTO();
                icon.setId(file.substring(0, file.length() - 4));
                list.add(icon);
            }
        }
        return new MapIconResult(list);
    }
}
