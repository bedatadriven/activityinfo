package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.ReportDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.shared.command.UpdateReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.Report;

import com.google.inject.Inject;

public class UpdateReportDefHandler implements CommandHandler<UpdateReportDef> {

	private ReportDAO reportDAO;
	
    @Inject
	public UpdateReportDefHandler(ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}
	
	@Override
	public CommandResult execute(UpdateReportDef cmd, User user)
			throws CommandException {
		
		try {
			Report report = ReportParser.parseXml(cmd.getNewXml());
		} catch(Exception e) {
            e.printStackTrace();
			throw new CommandException();
		}
		reportDAO.updateXml(cmd.getId(), cmd.getNewXml());
		
		return null;
		
	}

}
