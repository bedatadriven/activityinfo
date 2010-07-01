/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.dozer.Mapper;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectList;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class GetProjectsHandler implements CommandHandler<GetProjects> {

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetProjectsHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetProjects cmd, User user) throws CommandException {
        List<Project> projects = em.createQuery("select p from Project p order by p.name").getResultList();
        List<ProjectDTO> dtos = new ArrayList<ProjectDTO>();
        for(Project project : projects) {
            dtos.add(mapper.map(project, ProjectDTO.class));
        }
        return new ProjectList(dtos);

    }
}
