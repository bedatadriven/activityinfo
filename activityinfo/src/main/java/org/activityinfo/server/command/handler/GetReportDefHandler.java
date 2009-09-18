package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.ReportDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.XmlResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetReportDefHandler implements CommandHandler<GetReportDef> {

	protected ReportDAO reportDAO;

	@Inject
	public void setReportDAO(ReportDAO dao) {
		this.reportDAO = dao;
	}
	
	@Override
	public CommandResult execute(GetReportDef cmd, User user)
			throws CommandException {
		
		return new XmlResult(reportDAO.getXmlById(cmd.getId()));

	}

}
