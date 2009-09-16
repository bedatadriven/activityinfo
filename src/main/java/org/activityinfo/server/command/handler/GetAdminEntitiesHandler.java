package org.activityinfo.server.command.handler;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.dao.AdminDAO;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import com.google.inject.Inject;

public class GetAdminEntitiesHandler implements CommandHandler<GetAdminEntities>{


	protected AdminDAO adminDAO;
	protected Mapper mapper;


	@Inject
	public GetAdminEntitiesHandler(AdminDAO adminDAO, Mapper mapper) {
		this.adminDAO = adminDAO;
		this.mapper = mapper;
	}


	@Override
	public CommandResult execute(GetAdminEntities cmd, User user) throws CommandException {
		List<AdminEntity> entities;
        if(cmd.getActivityId()==null) {
            entities = adminDAO.getEntities(cmd.getLevelId(), cmd.getParentId());
        } else {
            entities = adminDAO.getEntities(cmd.getLevelId(), cmd.getParentId(), cmd.getActivityId());
        }
        List<AdminEntityModel> models = new ArrayList<AdminEntityModel>();

		for(AdminEntity entity : entities) {
			models.add( mapper.map(entity, AdminEntityModel.class) );
		}

		return new AdminEntityResult(models);
	}


}
