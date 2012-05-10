package org.activityinfo.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Project;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.database.hibernate.entity.UserPermission;
import org.activityinfo.shared.command.RemoveProject;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.activityinfo.shared.exception.ProjectHasSitesException;

import com.google.inject.Inject;

public class RemoveProjectHandler implements CommandHandler<RemoveProject> {

    private EntityManager em;

    @Inject
    public RemoveProjectHandler(EntityManager em) {
        this.em = em;
    }
	
	@Override
	public CommandResult execute(RemoveProject cmd, User user)
			throws CommandException {
        // verify the current user has access to this site
        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
        if (db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if (perm == null || perm.isAllowDesign()) {
                throw new IllegalAccessCommandException();
            }
        }

        // check to see if there are already sites associated with this
        // partner

        int siteCount = ((Number) em.createQuery("select count(s) from Site s where " +
                "s.activity.id in (select a.id from Activity a where a.database.id = :dbId) and " +
                "s.project.id = :projectId and " +
                "s.dateDeleted is null")
                .setParameter("dbId", cmd.getDatabaseId())
                .setParameter("projectId", cmd.getProjectId())
                .getSingleResult()).intValue();

        if (siteCount > 0) {
            throw new ProjectHasSitesException();
        }

        db.getPartners().remove(em.getReference(Project.class, cmd.getProjectId()));

        db.setLastSchemaUpdate(new Date());
        em.persist(db);

        return new VoidResult();
	}

}
