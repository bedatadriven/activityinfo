/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.persistence.EntityManager;
import org.dozer.Mapper;
import org.sigmah.server.policy.AmendmentPolicy;
import org.sigmah.shared.command.AmendmentAction;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.logframe.LogFrame;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

/**
 * Handle actions made on amendments.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AmendmentActionHandler implements CommandHandler<AmendmentAction> {

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public AmendmentActionHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(AmendmentAction cmd, User user) throws CommandException {
        final Project project = em.find(Project.class, cmd.getProjectId());

        if(Arrays.binarySearch(project.getAmendmentState().getActions(), cmd.getAction()) == -1)
            throw new IllegalStateException("The action '"+cmd.getAction()+"' cannot be applied on the project '"+project.getName()+"' (state "+project.getAmendmentState()+')');

        final AmendmentPolicy policy = new AmendmentPolicy(em);

        switch(cmd.getAction()) {
            case CREATE:
                // Changes the project state to draft and save the current state as a new amendment.
                final Amendment newAmendment = policy.createAmendment(project);

                int version = project.getAmendmentVersion() + 1;
                int revision = 1;

                // If the previous amendment is in the REJECTED state, then the current amendment is a new revision.
                if(project.getAmendments() != null &&
                        project.getAmendments().size() > 0 && 
                        project.getAmendments().get(project.getAmendments().size()-1).getState() == Amendment.State.REJECTED)
                    revision = project.getAmendments().get(project.getAmendments().size()-1).getRevision() + 1;

                // Updating the project
                project.setAmendmentVersion(version);
                project.setAmendmentRevision(revision);
                project.setAmendmentState(Amendment.State.DRAFT);

                project.getAmendments().add(newAmendment);
                em.merge(project);

                break;

            case LOCK:
                project.setAmendmentState(Amendment.State.LOCKED);
                em.merge(project);
                break;

            case UNLOCK:
                project.setAmendmentState(Amendment.State.DRAFT);
                em.merge(project);
                break;
                
            case REJECT:
                // Restore the active state or "creates" a new draft if no active state exists.
                project.setAmendmentState(Amendment.State.REJECTED);
                final Amendment rejectedAmendment = policy.createAmendment(project);

                if(project.getAmendments() != null) {
                    final Iterator<Amendment> iterator = project.getAmendments().iterator();

                    while(iterator.hasNext()) {
                        final Amendment amendment = iterator.next();

                        if(amendment.getState() == Amendment.State.ACTIVE) {
                            final LogFrame previousLogFrame = project.getLogFrame();
                            final LogFrame currentLogFrame = amendment.getLogFrame();

                            previousLogFrame.setParentProject(null);
                            currentLogFrame.setParentProject(project);

                            project.setLogFrame(currentLogFrame);
                            project.setAmendmentVersion(amendment.getVersion());
                            project.setAmendmentRevision(amendment.getRevision());
                            project.setAmendmentState(Amendment.State.ACTIVE);

                            // TODO: Restores values of flexible elements from history tokens

                            // Deleting the current amendment from the project
                            amendment.setLogFrame(null);
                            iterator.remove();

                            // Persisting changes
                            em.remove(previousLogFrame);
                            em.remove(amendment);
                        }
                    }
                    
                }
                
                if(project.getAmendments() == null)
                    project.setAmendments(new ArrayList<Amendment>());

                project.getAmendments().add(rejectedAmendment);
                em.merge(project);

                break;
                
            case VALIDATE:
                // Archive the active state (if one does exist) and activate the current one.
                for(final Amendment amendment : project.getAmendments()) {
                    if(amendment.getState() == Amendment.State.ACTIVE) {
                        amendment.setState(Amendment.State.ARCHIVED);
                        em.merge(amendment);
                    }
                }

                project.setAmendmentState(Amendment.State.ACTIVE);
                em.merge(project);
                break;
        }

        return mapper.map(project, ProjectDTO.class);
    }

}
