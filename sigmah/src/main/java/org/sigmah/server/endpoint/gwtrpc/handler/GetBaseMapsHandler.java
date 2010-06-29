/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
