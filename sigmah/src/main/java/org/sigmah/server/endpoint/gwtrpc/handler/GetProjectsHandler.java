/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.dozer.Mapper;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetProjectsHandler implements CommandHandler<GetProjects> {

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetProjectsHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    /**
     * Gets the projects list from the database.
     * 
     * @param cmd
     * @param user
     * 
     * @return a {@link CommandResult} object containing the {@link ProjectListResult} object.
     */
    @Override
    public CommandResult execute(GetProjects cmd, User user) throws CommandException {
    	/* Initialization */
    	List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();
    	
    	/* Recovery of the projects list from the DB */
        List<Project> projects = em.createQuery("select p from Project p order by p.name").getResultList();
        
        /* Transformation into DTO objects */
        for(Project project : projects) {
        	projectDTOList.add(mapper.map(project, ProjectDTO.class));
        }
        
        return new ProjectListResult(projectDTOList);

    }
}
