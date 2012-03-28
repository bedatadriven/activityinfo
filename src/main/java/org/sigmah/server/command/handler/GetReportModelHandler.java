package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

import org.sigmah.client.page.report.json.ReportJsonFactory;
import org.sigmah.client.page.report.json.ReportSerializer;
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
	private final ReportSerializer reportSerializer;

	@Inject
	public GetReportModelHandler(EntityManager em,
			ReportSerializer reportSerializer) {
		this.em = em;
		this.reportSerializer = reportSerializer;
	}

	@Override
	public CommandResult execute(GetReportModel cmd, User user)
			throws CommandException {

		ReportDefinition entity = em.find(ReportDefinition.class,
				cmd.getReportId());
		
		Report report = new Report();
		
		String json = entity.getJson();
		if (json.length() != 0) {
			report = reportSerializer.deserialize(entity.getJson());
		} else {
			try {
				report = ReportParserJaxb.parseXml(entity.getXml());
			} catch (JAXBException e) {
				throw new UnexpectedCommandException(e);
			}
		}

		report.setId(cmd.getReportId());
		return report;

	}

}
