package org.sigmah.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;
import org.sigmah.server.database.hibernate.entity.Target;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;
import org.sigmah.server.database.hibernate.entity.UserPermission;
import org.sigmah.shared.command.RemoveTarget;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;
import com.google.inject.Inject;

public class RemoveTargetHandler implements CommandHandler<RemoveTarget> {

    private EntityManager em;

    @Inject
    public RemoveTargetHandler(EntityManager em) {
        this.em = em;
    }
	
	@Override
	public CommandResult execute(RemoveTarget cmd, User user)
			throws CommandException {
        // verify the current user has access to this site
        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
        if (db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if (perm == null || perm.isAllowDesign()) {
                throw new IllegalAccessCommandException();
            }
        }


        db.getTargets().remove(em.getReference(Target.class, cmd.getTargetId()));
        em.remove(em.getReference(Target.class, cmd.getTargetId()));
       
        db.setLastSchemaUpdate(new Date());
        em.persist(db);

        return new VoidResult();
	}

}