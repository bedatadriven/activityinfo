/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.ParseException;
import org.sigmah.shared.report.model.Report;

import com.google.inject.Inject;

public class CreateReportDefHandler implements CommandHandler<CreateReportDef> {
    private EntityManager em;
    private Report report;
    private ReportDefinition reportDef;

    @Inject
    public CreateReportDefHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(CreateReportDef cmd, User user)
            throws CommandException {

        // verify that the XML is valid
        try {
        	reportDef= new ReportDefinition();
        	if(cmd.getXml() != null){
                report = ReportParserJaxb.parseXml(cmd.getXml());
                reportDef.setXml(cmd.getXml());
        	}else{
                report = cmd.getReport();
                String xml = ReportParserJaxb.createXML(report);
                reportDef.setXml(xml);
        	}
 	
            reportDef.setDatabase(em.getReference(UserDatabase.class, cmd.getDatabaseId()));
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
