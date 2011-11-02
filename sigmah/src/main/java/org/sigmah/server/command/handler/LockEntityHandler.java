package org.sigmah.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.sigmah.server.database.hibernate.entity.Activity;
import org.sigmah.server.database.hibernate.entity.LockedPeriod;
import org.sigmah.server.database.hibernate.entity.Project;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;
import org.sigmah.shared.command.LockEntity;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class LockEntityHandler implements CommandHandler<LockEntity> {
    private EntityManager em;

	@Inject
	public LockEntityHandler(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public CommandResult execute(LockEntity cmd, User user)
			throws CommandException {
		
		Activity activity = null;
		UserDatabase database = null;
		Project project = null;
		int databaseId = 0;
		
		LockedPeriod  lockedPeriod = new LockedPeriod();
		LockedPeriodDTO lockedPeriodDTO = cmd.getLockedPeriod();
		lockedPeriod.setFromDate(lockedPeriodDTO.getFromDate().atMidnightInMyTimezone());
		lockedPeriod.setToDate(lockedPeriodDTO.getToDate().atMidnightInMyTimezone());
		lockedPeriod.setName(lockedPeriodDTO.getName());
		lockedPeriod.setEnabled(lockedPeriodDTO.isEnabled());

		if (cmd.getUserDatabseId() != 0) {
	        database = em.find(UserDatabase.class, cmd.getUserDatabseId());
	        lockedPeriod.setUserDatabase(database);
	        databaseId = database.getId();
		}
		if (cmd.getProjectId() != 0) {
			project = em.find(Project.class, cmd.getProjectId());
			lockedPeriod.setProject(project);
			databaseId = project.getUserDatabase().getId();
		}
		if (cmd.getActivityId() != 0) {
			activity = em.find(Activity.class, cmd.getActivityId());
			lockedPeriod.setActivity(activity);
			databaseId = activity.getDatabase().getId();
		}
		
        UserDatabase db = em.find(UserDatabase.class, databaseId);

		em.persist(lockedPeriod);
		
        db.setLastSchemaUpdate(new Date());
        em.persist(db);
		
		if (database != null) {
			database.getLockedPeriods().add(lockedPeriod);
		}
		if (project != null) {
			project.getLockedPeriods().add(lockedPeriod);
		}
		if (activity != null) {
			activity.getLockedPeriods().add(lockedPeriod);
		}

        return new CreateResult(lockedPeriod.getId());
	}

}
