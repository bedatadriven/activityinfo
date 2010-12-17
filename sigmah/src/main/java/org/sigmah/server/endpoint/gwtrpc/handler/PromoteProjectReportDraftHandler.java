/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import java.util.Date;
import javax.persistence.EntityManager;
import org.sigmah.shared.command.PromoteProjectReportDraft;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.report.ProjectReport;
import org.sigmah.shared.domain.report.ProjectReportVersion;
import org.sigmah.shared.exception.CommandException;

/**
 * Handler for the {@link PromoteProjectReportDraft} command.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class PromoteProjectReportDraftHandler implements CommandHandler<PromoteProjectReportDraft> {
    private EntityManager em;

    @Inject
    public PromoteProjectReportDraftHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(PromoteProjectReportDraft cmd, User user) throws CommandException {
        final ProjectReport report = em.find(ProjectReport.class, cmd.getReportId());
        final ProjectReportVersion version = em.find(ProjectReportVersion.class, cmd.getVersionId());

        version.setVersion(report.getCurrentVersion().getVersion() + 1);
        version.setEditDate(new Date());
        version.setEditor(user);
        report.setCurrentVersion(version);

        em.merge(report);

        return GetProjectReportHandler.toDTO(report, version);
    }
}
