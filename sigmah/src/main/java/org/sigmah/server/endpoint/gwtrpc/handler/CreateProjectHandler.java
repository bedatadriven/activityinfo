package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.shared.domain.Country;
import org.sigmah.server.domain.Phase;
import org.sigmah.server.domain.PhaseModel;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.ProjectModel;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.command.CreateProject;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * Creates a project instance for the given model.
 * 
 * @author tmi
 * 
 */
public class CreateProjectHandler implements CommandHandler<CreateProject> {

    private static final Log log = LogFactory.getLog(CreateProjectHandler.class);

    private final EntityManager em;

    @Inject
    public CreateProjectHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(CreateProject cmd, User user) throws CommandException {

        if (log.isDebugEnabled()) {
            log.debug("[execute] Starting project creation.");
        }

        // Gets country.
        final Country country = em.find(Country.class, cmd.getCountryId());

        if (log.isDebugEnabled()) {
            log.debug("[execute] Found country with id=" + country.getId() + ".");
        }

        // Gets model.
        final ProjectModel model = em.find(ProjectModel.class, cmd.getModelId());

        if (log.isDebugEnabled()) {
            log.debug("[execute] Found project model with id=" + model.getId() + ".");
        }

        // Creates project instance.
        final Project project = new Project();

        // Userdatabase attributes.
        project.setStartDate(new Date());
        project.setCountry(country);
        project.setOwner(user);
        project.setName(cmd.getName());
        project.setFullName(cmd.getFullName());
        project.setLastSchemaUpdate(new Date());

        // Project attributes.
        project.setProjectModel(model);
        project.setLogFrame(null);

        // Creates and adds phases.
        for (final PhaseModel phaseModel : model.getPhases()) {

            final Phase phase = new Phase();
            phase.setModel(phaseModel);

            project.addPhase(phase);

            if (log.isDebugEnabled()) {
                log.debug("[execute] Creates and adds phase instance for model: " + phaseModel.getName() + ".");
            }

            // Searches the root phase.
            if (phaseModel.getId() == model.getRootPhase().getId()) {

                // Sets it.
                phase.setStartDate(new Date());
                project.setCurrentPhase(phase);

                if (log.isDebugEnabled()) {
                    log.debug("[execute] Sets the first phase: " + phaseModel.getName() + ".");
                }
            }
        }

        // Saves the project.
        em.persist(project);

        if (log.isDebugEnabled()) {
            log.debug("[execute] Project successfully created.");
        }

        return new VoidResult();
    }
}
