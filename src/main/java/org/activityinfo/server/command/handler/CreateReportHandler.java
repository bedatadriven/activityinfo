/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import org.activityinfo.client.page.report.json.ReportJsonFactory;
import org.activityinfo.server.database.hibernate.entity.ReportDefinition;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.shared.command.CreateReport;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.ParseException;

import com.google.inject.Inject;

public class CreateReportHandler implements CommandHandler<CreateReport> {
    private EntityManager em;
	private final ReportJsonFactory reportJsonFactory;

    private ReportDefinition reportDef;

    @Inject
    public CreateReportHandler(EntityManager em, ReportJsonFactory reportJsonFactory) {
        this.em = em;
        this.reportJsonFactory = reportJsonFactory;
    }

    @Override
    public CommandResult execute(CreateReport cmd, User user)
            throws CommandException {

        // verify that the XML is valid
        try {
        	reportDef = new ReportDefinition();
            
        	// TODO should allow null to xml field 
        	String xml = ReportParserJaxb.createXML(cmd.getReport());
            reportDef.setXml(xml);
 	
        	if(cmd.getDatabaseId() != null) { 
        		reportDef.setDatabase(em.getReference(UserDatabase.class, cmd.getDatabaseId()));
        	} 
        	
            reportDef.setTitle(cmd.getReport().getTitle());
            reportDef.setDescription(cmd.getReport().getDescription());
            reportDef.setOwner(user);
            reportDef.setVisibility(1);

            em.persist(reportDef);

            return new CreateResult(reportDef.getId());

        } catch (JAXBException e) {
            throw new ParseException(e.getMessage());
        }

    }
}
