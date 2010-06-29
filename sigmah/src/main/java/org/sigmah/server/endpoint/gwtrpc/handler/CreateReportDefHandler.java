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

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.ReportDefinition;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.UserDatabase;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.ParseException;
import org.sigmah.shared.report.model.Report;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

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
            Report report = ReportParserJaxb.parseXml(cmd.getXml());

            ReportDefinition reportDef = new ReportDefinition();
            reportDef.setDatabase(em.getReference(UserDatabase.class, cmd.getDatabaseId()));
            reportDef.setXml(cmd.getXml());
            reportDef.setTitle(report.getTitle());
            reportDef.setDescription(report.getDescription());
            reportDef.setFrequency(report.getFrequency());
            reportDef.setDay(report.getDay());
            reportDef.setOwner(user);
            reportDef.setVisibility(1);

            em.persist(reportDef);

            return new CreateResult(reportDef.getId());

        } catch (JAXBException e) {
            throw new ParseException(e.getMessage());
        }

    }
}
