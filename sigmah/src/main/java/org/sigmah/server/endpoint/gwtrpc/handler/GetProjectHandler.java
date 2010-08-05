/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import javax.persistence.EntityManager;

import org.dozer.Mapper;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class GetProjectHandler implements CommandHandler<GetProject> {

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetProjectHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    /**
     * Gets a project from the database and maps it into a {@link ProjectDTO} object.
     * 
     * @param cmd command containing the project id
     * @param user user connected
     * 
     * @return the {@link ProjectDTO} object.
     */
    @Override
    public CommandResult execute(GetProject cmd, User user) throws CommandException {
    	if (Log.isDebugEnabled()) {
    		Log.debug("[execute] Getting projet id#" + cmd.getProjectId() + " from the database.");
    	}
    	
    	Project project = em.find(Project.class, cmd.getProjectId());
    	
        return mapper.map(project, ProjectDTO.class);

    }
}
