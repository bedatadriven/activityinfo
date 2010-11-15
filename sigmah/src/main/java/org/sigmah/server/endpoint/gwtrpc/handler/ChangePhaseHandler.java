package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.ChangePhase;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.Phase;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * The handler for {@link ChangePhase} command.
 * 
 * @author tmi
 * 
 */
public class ChangePhaseHandler implements CommandHandler<ChangePhase> {

    private final static Log LOG = LogFactory.getLog(ChangePhaseHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public ChangePhaseHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(ChangePhase cmd, User user) throws CommandException {

        // Gets the project.
        Project project = em.find(Project.class, cmd.getProjectId());

        // Gets the current phase.
        final Phase currentPhase = project.getCurrentPhase();

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Closing the current phase #" + currentPhase.getId() + ".");
        }

        // Closes the current phase.
        currentPhase.setEndDate(new Date());

        // If the id of the phase to activate isn't null, activates it.
        if (cmd.getPhaseId() != null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Try to activate phase #" + cmd.getPhaseId() + ".");
            }

            // Searches for the given phase phase.
            Phase newCurrentPhase = null;
            for (final Phase phase : project.getPhases()) {
                if (phase.getId() == (long) cmd.getPhaseId()) {
                    newCurrentPhase = phase;
                }
            }

            if (newCurrentPhase == null) {
                // The activated phase cannot be found: error.
                LOG.error("[execute] The phase with id #" + cmd.getPhaseId() + " doesn't exist.");
                throw new CommandException("The phase to activate doesn't exist.");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Activates the new phase #" + cmd.getPhaseId() + ".");
            }

            // Activates the new phase.
            newCurrentPhase.setStartDate(new Date());
            project.setCurrentPhase(newCurrentPhase);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Saves modifications.");
        }

        // Saves the new project state.
        project = em.merge(project);

        return mapper.map(project, ProjectDTO.class);
    }
}
