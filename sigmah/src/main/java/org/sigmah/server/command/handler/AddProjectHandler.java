package org.sigmah.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.sigmah.shared.command.AddProject;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;


/*
 * Adds given Project to the database
 */
public class AddProjectHandler implements CommandHandler<AddProject> {

    private final EntityManager em;

    @Inject
	public AddProjectHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(AddProject cmd, User user)
			throws CommandException {

        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());

        ProjectDTO from = cmd.getProjectDTO();
        Project project = new Project();
        project.setName(from.getName());
        project.setDescription(from.getDescription());
        project.setUserDatabase(db);

        db.setLastSchemaUpdate(new Date());
        
        em.persist(project);
        em.persist(db);
        db.getProjects().add(project);

        return new CreateResult(project.getId());
	}
}