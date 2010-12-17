/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import org.sigmah.shared.command.RemoveProjectReportDraft;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.report.ProjectReportVersion;
import org.sigmah.shared.exception.CommandException;

/**
 * Handler for the {@link RemoveProjectReportDraft} command.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class RemoveProjectReportDraftHandler implements CommandHandler<RemoveProjectReportDraft> {
    private EntityManager em;

    @Inject
    public RemoveProjectReportDraftHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(RemoveProjectReportDraft cmd, User user) throws CommandException {
        final ProjectReportVersion version = em.find(ProjectReportVersion.class, cmd.getVersionId());
        em.remove(version);
        
        return null;
    }

}
