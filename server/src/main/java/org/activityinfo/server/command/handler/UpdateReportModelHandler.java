package org.activityinfo.server.command.handler;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.activityinfo.client.page.report.json.ReportJsonFactory;
import org.activityinfo.server.database.hibernate.entity.ReportDefinition;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.shared.command.UpdateReportModel;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

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
				
		Query query = em
				.createQuery(
						"select r from ReportDefinition r where r.id in (:id)")
				.setParameter("id", cmd.getModel().getId());

		ReportDefinition result = (ReportDefinition) query.getSingleResult();
		
		result.setTitle(cmd.getModel().getTitle());
//		result.setJson(cmd.getReportJsonModel());
		try{
			result.setXml(ReportParserJaxb.createXML(cmd.getModel()));
		} catch (JAXBException e) {
			throw new UnexpectedCommandException(e);
		}
		
		em.persist(result);
		
		return null;
	}

}
