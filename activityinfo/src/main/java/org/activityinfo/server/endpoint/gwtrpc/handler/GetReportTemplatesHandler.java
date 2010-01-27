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
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.exception.CommandException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetReportTemplates
 */
public class GetReportTemplatesHandler implements CommandHandler<GetReportTemplates> {

    private EntityManager em;

    @Inject
    public GetReportTemplatesHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(GetReportTemplates cmd, User user) throws CommandException {

        Query query = em.createQuery("select r from ReportDefinition r");

        List<ReportDefinition> results = query.getResultList();

        List<ReportTemplateDTO> dtos = new ArrayList<ReportTemplateDTO>();

        for (ReportDefinition template : results) {

            ReportTemplateDTO dto = new ReportTemplateDTO();
            dto.setId(template.getId());
            dto.setDatabaseName(template.getDatabase() == null ? null : template.getDatabase().getName());
            dto.setOwnerName(template.getOwner().getName());
            dto.setAmOwner(template.getOwner().getId() == user.getId());
            dto.setTitle(template.getTitle());
            dto.setFrequency(template.getFrequency());
            dto.setDay(template.getDay());
            dto.setDescription(template.getDescription());
            dto.setEditAllowed(dto.getAmOwner());

            dto.setSubscribed(false);
            for (ReportSubscription sub : template.getSubscriptions()) {     // todo: this is ridiculous.
                if (sub.getUser().getId() == user.getId()) {
                    dto.setSubscribed(sub.isSubscribed());
                    break;
                }
            }
            dtos.add(dto);
        }
        return new ReportTemplateResult(dtos);
    }
}
