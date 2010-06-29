/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.ReportDefinition;
import org.sigmah.server.domain.User;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.UpdateReportDef;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.ParseException;
import org.sigmah.shared.report.model.Report;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.UpdateReportDef
 */
public class UpdateReportDefHandler implements CommandHandler<UpdateReportDef> {

    private final EntityManager em;

    @Inject
    public UpdateReportDefHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(UpdateReportDef cmd, User user)
            throws CommandException {

        try {
            Report report = ReportParserJaxb.parseXml(cmd.getNewXml());

            ReportDefinition def = em.find(ReportDefinition.class, cmd.getId());
            def.setXml(cmd.getNewXml());

            // push certain properties down into the table so
            // we don't have to parse the XML when generating a list
            def.setTitle(report.getTitle());
            def.setDescription(report.getDescription());
            def.setFrequency(report.getFrequency());
            def.setDay(report.getDay());

        } catch (JAXBException e) {
            e.printStackTrace();
            throw new ParseException(e.getMessage());
        }

        return null;

    }

}
