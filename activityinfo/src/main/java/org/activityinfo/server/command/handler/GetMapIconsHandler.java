package org.activityinfo.server.command.handler;

import org.activityinfo.shared.command.GetMapIcons;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.MapIconResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.dto.MapIconModel;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.MapIconPath;
import com.google.inject.Inject;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class GetMapIconsHandler implements CommandHandler<GetMapIcons> {

    private String mapIconPath;

    @Inject
    public GetMapIconsHandler(@MapIconPath String mapIconPath) {
        this.mapIconPath = mapIconPath;
    }

    public CommandResult execute(GetMapIcons cmd, User user) throws CommandException {

        File iconFolder = new File(mapIconPath);

        List<MapIconModel> list = new ArrayList<MapIconModel>();

        for(String file : iconFolder.list()) {
            if(file.endsWith(".png")) {
                MapIconModel icon = new MapIconModel();
                icon.setId(file.substring(0, file.length()-4));
                list.add(icon);
            }
        }
        return new MapIconResult(list);
    }
}
