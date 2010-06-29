/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.ReportDefinition;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.UserDatabase;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.ParseException;
import org.sigmah.shared.report.model.Report;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

public class CreateReportDefHandler implements CommandHandler<CreateReportDef> {
    private EntityManager em;

    @Inject
    public CreateReportDefHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(CreateReportDef cmd, User user)
            throws CommandException {

        // verify that the XML is valid
        try {
            Report report = ReportParserJaxb.parseXml(cmd.getXml());

            ReportDefinition reportDef = new ReportDefinition();
            reportDef.setDatabase(em.getReference(UserDatabase.class, cmd.getDatabaseId()));
            reportDef.setXml(cmd.getXml());
            reportDef.setTitle(report.getTitle());
            reportDef.setDescription(report.getDescription());
            reportDef.setFrequency(report.getFrequency());
            reportDef.setDay(report.getDay());
            reportDef.setOwner(user);
            reportDef.setVisibility(1);

            em.persist(reportDef);

            return new CreateResult(reportDef.getId());

        } catch (JAXBException e) {
            throw new ParseException(e.getMessage());
        }

    }
}
