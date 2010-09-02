package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.server.domain.ProjectModel;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetProjectModels;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectModelListResult;
import org.sigmah.shared.dto.ProjectModelDTOLight;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * Retrieves the list of project models available to the user.
 * 
 * @author tmi
 * 
 */
public class GetProjectModelsHandler implements CommandHandler<GetProjectModels> {

    private static final Log log = LogFactory.getLog(GetProjectModelsHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetProjectModelsHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetProjectModels cmd, User user) throws CommandException {

        final ArrayList<ProjectModelDTOLight> projectModelDTOList = new ArrayList<ProjectModelDTOLight>();

        // Creates selection query.
        final Query query = em.createQuery("SELECT m FROM ProjectModel m ORDER BY m.name");

        // Gets all project models entities.
        @SuppressWarnings("unchecked")
        final List<ProjectModel> models = (List<ProjectModel>) query.getResultList();

        // Mapping (entity -> dto).
        if (models != null) {
            for (ProjectModel model : models) {
                projectModelDTOList.add(mapper.map(model, ProjectModelDTOLight.class));
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("[execute] found " + projectModelDTOList.size() + " project models.");
        }

        return new ProjectModelListResult(projectModelDTOList);
    }

}
