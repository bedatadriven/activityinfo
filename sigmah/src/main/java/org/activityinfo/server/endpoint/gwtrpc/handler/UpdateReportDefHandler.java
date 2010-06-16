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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.activityinfo.server.domain.ReportDefinition;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.shared.command.UpdateReportDef;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.ParseException;
import org.activityinfo.shared.report.model.Report;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.UpdateReportDef
 */
public class UpdateReportDefHandler implements CommandHandler<UpdateReportDef> {

    private final EntityManager em;

    @Inject
    public UpdateReportDefHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(UpdateReportDef cmd, User user)
            throws CommandException {

        try {
            Report report = ReportParserJaxb.parseXml(cmd.getNewXml());

            ReportDefinition def = em.find(ReportDefinition.class, cmd.getId());
            def.setXml(cmd.getNewXml());

            // push certain properties down into the table so
            // we don't have to parse the XML when generating a list
            def.setTitle(report.getTitle());
            def.setDescription(report.getDescription());
            def.setFrequency(report.getFrequency());
            def.setDay(report.getDay());

        } catch (JAXBException e) {
            e.printStackTrace();
            throw new ParseException(e.getMessage());
        }

        return null;

    }

}
