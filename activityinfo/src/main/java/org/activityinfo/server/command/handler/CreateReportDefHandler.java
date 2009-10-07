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
import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.shared.command.CreateReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.exception.CommandException;

import javax.persistence.EntityManager;

public class CreateReportDefHandler implements CommandHandler<CreateReportDef> {
    private EntityManager em;

    @Inject
    public CreateReportDefHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(CreateReportDef cmd, User user)
            throws CommandException {

        // verify that the XML is valid
        try {
            ReportParser.parseXml(cmd.getXml());
        } catch (Exception e) {
            throw new CommandException("Invalid XML");
        }


        ReportTemplate reportTemplate = new ReportTemplate();
        if (cmd.getDatabaseId() != null) {
            reportTemplate.setDatabase(em.getReference(UserDatabase.class, cmd.getDatabaseId()));
        }
        reportTemplate.setXml(cmd.getXml());
        reportTemplate.setOwner(user);
        reportTemplate.setVisibility(1);

        em.persist(reportTemplate);


        return new CreateResult(reportTemplate.getId());
    }
}
