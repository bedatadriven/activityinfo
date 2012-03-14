package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.sigmah.client.page.report.json.ReportJsonFactory;
import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.UpdateReportModel;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.UnexpectedCommandException;
import org.sigmah.shared.report.model.Report;

import com.google.inject.Inject;

public class UpdateReportModelHandler implements CommandHandler<UpdateReportModel> {
	
	private final EntityManager em;
	private final ReportJsonFactory reportJsonFactory;
	
	@Inject
	public UpdateReportModelHandler(EntityManager em, ReportJsonFactory reportJsonFactory){
		this.em = em;
		this.reportJsonFactory = reportJsonFactory;
	}

	@Override
	public CommandResult execute(UpdateReportModel cmd, User user)
			throws CommandException {
		
		Report report = reportJsonFactory.deserialize(cmd.getReportJsonModel());
		
		Query query = em
				.createQuery(
						"select r from ReportDefinition r where r.id in (:id)")
				.setParameter("id", report.getId());

		ReportDefinition result = (ReportDefinition) query.getSingleResult();
		
		result.setTitle(report.getTitle());
		result.setJson(cmd.getReportJsonModel());
//		try{
//			result.setXml(ReportParserJaxb.createXML(cmd.getModel()));
//		} catch (JAXBException e) {
//			throw new UnexpectedCommandException(e);
//		}
		
		em.persist(result);
		
		return null;
	}

}
