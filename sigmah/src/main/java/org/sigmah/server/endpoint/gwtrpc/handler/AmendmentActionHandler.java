/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import java.util.Arrays;
import javax.persistence.EntityManager;
import org.sigmah.server.policy.AmendmentPolicy;
import org.sigmah.shared.command.AmendmentAction;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.domain.Amendment.State;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

/**
 * Handle actions made on amendments.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AmendmentActionHandler implements CommandHandler<AmendmentAction> {

    private final EntityManager em;

    @Inject
    public AmendmentActionHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(AmendmentAction cmd, User user) throws CommandException {
        final Project project = em.find(Project.class, cmd.getProjectId());

        if(Arrays.binarySearch(project.getAmendmentState().getActions(), cmd.getAction()) == -1)
            throw new IllegalStateException("The action '"+cmd.getAction()+"' cannot be applied on the project '"+project.getName()+"' (state "+project.getAmendmentState()+')');

        Amendment.State result = null;

        switch(cmd.getAction()) {
            case CREATE:
                final AmendmentPolicy policy = new AmendmentPolicy(em);
                policy.createAmendment(project);
                result = project.getAmendmentState();
                break;

            case LOCK:
                project.setAmendmentState(State.LOCKED);
                result = State.LOCKED;
                em.merge(project);
                break;

            case UNLOCK:
                project.setAmendmentState(State.DRAFT);
                result = State.DRAFT;
                em.merge(project);
                break;
                
            case REJECT:
                // Restore the active state or "creates" a new draft if no active state exists.
                break;
                
            case VALIDATE:
                // Archive the active state (if one does exist) and activate the current one.
                project.setAmendmentState(State.ACTIVE);
                result = State.ACTIVE;
                em.merge(project);
                break;
        }

        return result;
    }

}
