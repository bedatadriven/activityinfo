/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.sigmah.shared.command.GetProjectReportModels;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectReportModelListResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.report.ProjectReportModel;
import org.sigmah.shared.exception.CommandException;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetProjectReportModelsHandler implements CommandHandler<GetProjectReportModels> {
    private EntityManager em;

    @Inject
    public GetProjectReportModelsHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(GetProjectReportModels cmd, User user) throws CommandException {
        final ArrayList<GetProjectReportModels.ModelReference> references = new ArrayList<GetProjectReportModels.ModelReference>();

        final Query query = em.createQuery("SELECT r FROM ProjectReportModel r");

        try {
            final List<ProjectReportModel> models = query.getResultList();

            for(final ProjectReportModel model : models)
                references.add(new GetProjectReportModels.ModelReference(model));

        } catch (NoResultException e) {
            // No reports in the current project
        }

        return new ProjectReportModelListResult(references);
    }

}
