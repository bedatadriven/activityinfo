/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.ReportDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.shared.command.UpdateReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.Report;

/**
 * @see org.activityinfo.shared.command.UpdateReportDef
 *
 * @author Alex Bertram
 */
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
