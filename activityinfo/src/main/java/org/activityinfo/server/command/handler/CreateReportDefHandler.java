package org.activityinfo.server.command.handler;

import javax.persistence.EntityManager;

import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.shared.command.CreateReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

public class CreateReportDefHandler implements CommandHandler<CreateReportDef> {
    private EntityManager em;

    @Inject
    public CreateReportDefHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(CreateReportDef cmd, User user)
            throws CommandException {

        ReportTemplate reportTemplate = new ReportTemplate();
        if (cmd.getDatabaseId() != null) {
            reportTemplate.setDatabase(em.getReference(UserDatabase.class, cmd.getDatabaseId()));
        }
        reportTemplate.setXml(cmd.getXml());
        reportTemplate.setOwner(user);
        reportTemplate.setVisibility(1);

        em.persist(reportTemplate);


        return new CreateResult(reportTemplate.getId());
    }
}
