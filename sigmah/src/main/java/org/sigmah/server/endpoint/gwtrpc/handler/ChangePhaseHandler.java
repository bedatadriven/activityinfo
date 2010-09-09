package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.server.domain.Phase;
import org.sigmah.server.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.command.ChangePhase;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

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

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Changing project active phase.");
        }

        // Gets project.
        Project project = em.find(Project.class, cmd.getProjectId());
        final Phase currentPhase = project.getCurrentPhase();

        // Searches the new active phase.
        Phase newActivePhase = null;
        for (final Phase phase : project.getPhases()) {
            if (phase.getId() == cmd.getPhaseId()) {
                newActivePhase = phase;
            }
        }

        if (newActivePhase == null) {
            // The activated phase cannot be found: error.
            throw new CommandException("The activated phase doesn't exist.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Find the activated phase ; id: " + newActivePhase.getId() + ".");
        }

        // Activates the new phase.
        newActivePhase.setStartDate(new Date());
        project.setCurrentPhase(newActivePhase);

        // Marks the last active phase as ended.
        currentPhase.setEndDate(new Date());

        // Saves the new project state.
        project = em.merge(project);

        final ArrayList<ProjectDTO> l = new ArrayList<ProjectDTO>();
        l.add(mapper.map(project, ProjectDTO.class));

        return new ProjectListResult(l);
    }

}
