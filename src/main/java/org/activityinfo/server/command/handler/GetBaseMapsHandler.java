package org.activityinfo.server.command.handler;

import org.activityinfo.shared.command.GetBaseMaps;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.BaseMapResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.server.dao.BaseMapDAO;
import org.activityinfo.server.domain.User;
import com.google.inject.Inject;
/*
 * @author Alex Bertram
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
