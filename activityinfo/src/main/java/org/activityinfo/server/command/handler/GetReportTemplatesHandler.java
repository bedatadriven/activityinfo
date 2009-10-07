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
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportMetadataParser;
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
 *
 * @see org.activityinfo.shared.command.GetReportTemplates
 *
 * @author Alex Bertram
 */
public class GetReportTemplatesHandler implements CommandHandler<GetReportTemplates> {

	private EntityManager em;
	
	@Inject
	public GetReportTemplatesHandler(EntityManager em) {
		this.em = em;
	}

    @Override
	public CommandResult execute(GetReportTemplates cmd, User user) throws CommandException {

		Query query = em.createQuery("select r from ReportTemplate r");

        
		List<ReportTemplate> results = query.getResultList();
		
		List<ReportTemplateDTO> dtos = new ArrayList<ReportTemplateDTO>();

		ReportMetadataParser parser = new ReportMetadataParser();
		
		for(ReportTemplate template : results) {

			ReportTemplateDTO dto = new ReportTemplateDTO();
			dto.setId(template.getId());
			dto.setDatabaseName(template.getDatabase() == null ? null : template.getDatabase().getName());
            dto.setOwnerName(template.getOwner().getName());
			dto.setAmOwner( template.getOwner().getId() == user.getId());
			dto.setEditAllowed(dto.getAmOwner());
            dto.setSubscriptionFrequency(0);
            dto.setSubscriptionDay(0);

            for(ReportSubscription sub : template.getSubscriptions()) {
                if(sub.getUser().getId() == user.getId()) {
                    dto.setSubscriptionFrequency(sub.getFrequency());
                    dto.setSubscriptionDay(sub.getDay());
                    break;
                }
            }

			try {
				parser.parse(dto, template.getXml());
			
			} catch(Exception e) {
				e.printStackTrace();
				// do nothing ?
			}

			dtos.add(dto);
        }
		return new ReportTemplateResult(dtos);
	}

}
