/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.shared.command.GetMapIcons;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.MapIconResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.MapIconDTO;
import org.sigmah.shared.exception.CommandException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetMapIcons
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
