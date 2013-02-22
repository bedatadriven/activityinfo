package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import com.google.inject.Inject;

public class UpdateReportModelHandler implements
    CommandHandler<UpdateReportModel> {

    private final EntityManager em;
    private final ReportJsonFactory reportJsonFactory;

    @Inject
    public UpdateReportModelHandler(final EntityManager em,
        final ReportJsonFactory reportJsonFactory) {
        this.em = em;
        this.reportJsonFactory = reportJsonFactory;
    }

    @Override
    public CommandResult execute(final UpdateReportModel cmd, final User user)
        throws CommandException {

        Query query = em
            .createQuery(
                "select r from ReportDefinition r where r.id in (:id)")
            .setParameter("id", cmd.getModel().getId());

        ReportDefinition result = (ReportDefinition) query.getSingleResult();
        if (result.getOwner().getId() != user.getId()) {
            throw new IllegalAccessCommandException(
                "Current user does not have the right to edit this report");
        }

        result.setTitle(cmd.getModel().getTitle());
        // result.setJson(cmd.getReportJsonModel());
        try {
            result.setXml(ReportParserJaxb.createXML(cmd.getModel()));
        } catch (JAXBException e) {
            throw new UnexpectedCommandException(e);
        }

        em.persist(result);

        return null;
    }

}
