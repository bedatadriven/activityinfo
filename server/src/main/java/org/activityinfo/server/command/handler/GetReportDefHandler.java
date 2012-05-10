/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import org.activityinfo.server.database.hibernate.dao.ReportDefinitionDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.XmlResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetReportDef
 */
public class GetReportDefHandler implements CommandHandler<GetReportDef> {

    private ReportDefinitionDAO reportDAO;

    @Inject
    public void setReportDAO(ReportDefinitionDAO dao) {
        this.reportDAO = dao;
    }

    @Override
    public CommandResult execute(GetReportDef cmd, User user)
            throws CommandException {
        return new XmlResult(reportDAO.findById(cmd.getId()).getXml());
    }

}
