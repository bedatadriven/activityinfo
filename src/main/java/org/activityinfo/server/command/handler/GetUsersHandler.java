package org.activityinfo.server.command.handler;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.PagingResult;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.dto.UserModel;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import com.google.inject.Inject;
import com.extjs.gxt.ui.client.Style;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GetUsersHandler implements CommandHandler<GetUsers> {


	protected EntityManager em;
	protected Mapper mapper;
	
	
	@Inject 
	public GetUsersHandler(EntityManager em, Mapper mapper) {
		this.em = em;
		this.mapper = mapper;
	}
	
	@Override
	public CommandResult execute(GetUsers cmd, User currentUser) throws CommandException {

		UserDatabase database = em.find(UserDatabase.class, cmd.getDatabaseId());
		
		if(database.getOwner().getId() != currentUser.getId()) {
			throw new CommandException();
		}

        String orderByClause="";

        if(cmd.getSortInfo().getSortDir()!= Style.SortDir.NONE) {
            String dir = cmd.getSortInfo().getSortDir() == Style.SortDir.ASC ? "asc" : "desc";
            String property = null;
            String field = cmd.getSortInfo().getSortField();

            if("name".equals(field)) {
                property = "up.user.name";
            } else if("email".equals(field)) {
                property = "up.user.email";
            } else if("partner".equals(field)) {
                property = "up.partner.name";
            } else if(field != null && field.startsWith("allow")) {
                property = "up." + field;
            }

            if(property != null) {
                orderByClause = " order by " + property + " " + dir;
            }                                                             

        }

		Query query = em.createQuery("select up from UserPermission up where up.allowView=true and up.database.id = ?1"
                                                    + orderByClause)
               	.setParameter(1, database.getId());

        if(cmd.getOffset()>0) {
            query.setFirstResult(cmd.getOffset());
        }
        if(cmd.getLimit()>0) {
            query.setMaxResults(cmd.getLimit());
        }

        List<UserPermission> perms = query.getResultList();

		
		List<UserModel> models = new ArrayList<UserModel>();
		
		for(UserPermission perm : perms) {
			models.add(mapper.map(perm, UserModel.class));		
		}
		
		int totalCount = ((Number)em.createQuery("select count(up) from UserPermission up where up.database.id = ?1")
                .setParameter(1, database.getId())
                .getSingleResult())
                .intValue();
		
		return new UserResult(models, cmd.getOffset(), totalCount);

	}

}
