package org.sigmah.server.endpoint.gwtrpc.handler;

import javax.persistence.EntityManager;

import org.sigmah.shared.command.AddProject;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.domain.Project2;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.dto.Project2DTO;
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
        
        Project2DTO from = cmd.getProject2DTO();
        Project2 project = new Project2();
        project.setName(from.getName());
        project.setDescription(from.getDescription());
        
        em.persist(project);
        db.getProjects().add(project);
        
        return new CreateResult(project.getId());
	}
}