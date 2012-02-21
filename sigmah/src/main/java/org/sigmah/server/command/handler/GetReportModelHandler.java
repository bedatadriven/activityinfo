package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.GetReportModel;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.UnexpectedCommandException;
import org.sigmah.shared.report.model.Report;

import com.google.inject.Inject;

public class GetReportModelHandler implements CommandHandler<GetReportModel> {
	private EntityManager em;

	@Inject
	public GetReportModelHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(GetReportModel cmd, User user)
			throws CommandException {

		ReportDefinition entity = em.find(ReportDefinition.class, cmd.getReportId());
		
		Report report = new Report();
		try {
			report = ReportParserJaxb.parseXml(entity.getXml());
		} catch (JAXBException e) {
			throw new UnexpectedCommandException(e);
		}
		report.setId(cmd.getReportId());
		
		return report;
		
	}

}
