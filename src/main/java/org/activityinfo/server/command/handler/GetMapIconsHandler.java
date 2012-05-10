/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

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
