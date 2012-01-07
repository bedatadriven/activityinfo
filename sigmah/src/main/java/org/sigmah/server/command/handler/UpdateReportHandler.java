package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.UpdateReport;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.Report;

import com.google.inject.Inject;

public class UpdateReportHandler implements CommandHandler<UpdateReport> {
	
	private final EntityManager em;
	
	@Inject
	public UpdateReportHandler(EntityManager em){
		this.em = em;
	}

	@Override
	public CommandResult execute(UpdateReport cmd, User user)
			throws CommandException {
		Query query = em
				.createQuery(
						"select r from ReportDefinition r where r.id in (:id)")
				.setParameter("id", cmd.getId());

		ReportDefinition result = (ReportDefinition) query.getSingleResult();
		if(cmd.getTitle() != null){
			result.setTitle(cmd.getTitle());
		}
		try{
			if(cmd.getElement() != null){
				Report report = new Report();
				
				report = ReportParserJaxb.parseXml(result.getXml());
				report.setElements(cmd.getElement());
				
				String xml = ReportParserJaxb.createXML(report);
				result.setXml(xml);	
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		em.persist(result);
		
		return null;
	}

}
