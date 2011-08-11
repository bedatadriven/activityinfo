package org.sigmah.server.endpoint.gwtrpc.handler;

import javax.persistence.EntityManager;

import org.sigmah.shared.command.LockEntity;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.LockedPeriod;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
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
		
		LockedPeriod  lockedPeriod = new LockedPeriod();
		LockedPeriodDTO lockedPeriodDTO = cmd.getLockedPeriod();
		lockedPeriod.setFromDate(lockedPeriodDTO.getFromDate());
		lockedPeriod.setToDate(lockedPeriodDTO.getToDate());
		lockedPeriod.setName(lockedPeriodDTO.getName());
		lockedPeriod.setEnabled(lockedPeriodDTO.isEnabled());

		if (cmd.getUserDatabseId() != 0) {
	        database = em.find(UserDatabase.class, cmd.getUserDatabseId());
	        lockedPeriod.setUserDatabase(database);
		}
		if (cmd.getProjectId() != 0) {
			project = em.find(Project.class, cmd.getProjectId());
			lockedPeriod.setProject(project);
		}
		if (cmd.getActivityId() != 0) {
			activity = em.find(Activity.class, cmd.getActivityId());
			lockedPeriod.setActivity(activity);
		}
		
		em.persist(lockedPeriod);
		
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
