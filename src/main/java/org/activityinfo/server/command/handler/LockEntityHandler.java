package org.activityinfo.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Activity;
import org.activityinfo.server.database.hibernate.entity.LockedPeriod;
import org.activityinfo.server.database.hibernate.entity.Project;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.LockEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.exception.CommandException;

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
		
		LockedPeriod  lockedPeriod = new LockedPeriod();
		LockedPeriodDTO lockedPeriodDTO = cmd.getLockedPeriod();
		lockedPeriod.setFromDate(lockedPeriodDTO.getFromDate().atMidnightInMyTimezone());
		lockedPeriod.setToDate(lockedPeriodDTO.getToDate().atMidnightInMyTimezone());
		lockedPeriod.setName(lockedPeriodDTO.getName());
		lockedPeriod.setEnabled(lockedPeriodDTO.isEnabled());

		int databaseId;
		if (cmd.getUserDatabseId() != 0) {
	        database = em.find(UserDatabase.class, cmd.getUserDatabseId());
	        lockedPeriod.setUserDatabase(database);
	        databaseId = database.getId();
		} else if (cmd.getProjectId() != 0) {
			project = em.find(Project.class, cmd.getProjectId());
			lockedPeriod.setProject(project);
			databaseId = project.getUserDatabase().getId();
		} else if (cmd.getActivityId() != 0) {
			activity = em.find(Activity.class, cmd.getActivityId());
			lockedPeriod.setActivity(activity);
			databaseId = activity.getDatabase().getId();
		} else {
			throw new CommandException("One of the following must be provdied: userDatabaseId, projectId, activityId");
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
