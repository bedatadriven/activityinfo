/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import org.sigmah.server.dao.BaseMapDAO;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.SateliteBaseMap;
import org.sigmah.shared.map.StreetBaseMap;

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
    	List<BaseMap> maps = new ArrayList<BaseMap>();
    	maps.addAll(baseMapDAO.getBaseMaps());
    	maps.add(new SateliteBaseMap());
    	maps.add(new StreetBaseMap());
        return new BaseMapResult(maps);
    }
}
