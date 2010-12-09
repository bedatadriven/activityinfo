package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.command.GetProjectDocuments;
import org.sigmah.shared.command.GetProjectReports.ReportReference;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectReportListResult;
import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.value.File;
import org.sigmah.shared.domain.value.FileVersion;
import org.sigmah.shared.domain.value.Value;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetProjectDocumentsHandler implements CommandHandler<GetProjectDocuments> {

    private EntityManager em;

    @Inject
    public GetProjectDocumentsHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandResult execute(GetProjectDocuments cmd, User user) throws CommandException {

        final ArrayList<ReportReference> references = new ArrayList<ReportReference>();

        if (cmd.getProjectId() == null || cmd.getElements() == null) {
            throw new IllegalArgumentException(
                    "GetProjectDocuments should specify a project id and a element ids list.");
        }

        // Query to retrieves the value of a files list element in this project.
        final Query valuesQuery = em
                .createQuery("SELECT v FROM Value v WHERE v.containerId = :projectId AND v.element.id = :elementId");

        // Query to retrieves all the files of a list.
        final Query filesQuery = em.createQuery("SELECT f FROM File f WHERE f.id IN (:idsList)");

        // For each files list.
        for (final GetProjectDocuments.FilesListElement fle : cmd.getElements()) {

            valuesQuery.setParameter("projectId", cmd.getProjectId());
            valuesQuery.setParameter("elementId", fle.getId());

            final List<Value> values = (List<Value>) valuesQuery.getResultList();

            if (values != null) {

                // For each value, retrieves the files list.
                for (final Value v : values) {

                    filesQuery.setParameter("idsList", ValueResultUtils.splitValuesAsInteger(v.getValue()));

                    final List<File> documents = (List<File>) filesQuery.getResultList();

                    if (documents != null) {
                        for (final File document : documents) {

                            final FileVersion lastVersion = document.getLastVersion();

                            final ReportReference r = new ReportReference();
                            r.setId(document.getId());
                            r.setDocument(true);
                            r.setName(lastVersion.getName() + '.' + lastVersion.getExtension());
                            r.setLastEditDate(lastVersion.getAddedDate());
                            r.setEditorName(User.getUserShortName(lastVersion.getAuthor()));
                            r.setPhaseName(fle.getPhaseName());
                            r.setFlexibleElementLabel(fle.getElementLabel());

                            references.add(r);
                        }
                    }
                }
            }
        }

        return new ProjectReportListResult(references);
    }
}
