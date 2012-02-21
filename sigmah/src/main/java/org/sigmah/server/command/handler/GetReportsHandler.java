/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetReports;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ReportsResult;
import org.sigmah.shared.dto.ReportMetadataDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetReports
 */
public class GetReportsHandler implements CommandHandler<GetReports> {
    private EntityManager em;

    @Inject
    public GetReportsHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(GetReports cmd, User user) throws CommandException {

    	// note that we are excluding reports with a null title-- these
    	// reports have not yet been explicitly saved by the user
        Query query = em.createQuery("select r from ReportDefinition r where r.owner.id = :userId and r.title is not null")
        	.setParameter("userId", user.getId());

        List<ReportDefinition> results = query.getResultList();
        List<ReportMetadataDTO> dtos = new ArrayList<ReportMetadataDTO>();

        for (ReportDefinition template : results) {

            ReportMetadataDTO dto = new ReportMetadataDTO();
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
        return new ReportsResult(dtos);
    }
}
