package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetUserReports;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ReportTemplateResult;
import org.sigmah.shared.dto.ReportDefinitionDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetUserReportsHandler implements CommandHandler<GetUserReports> {
	private EntityManager em;

	@Inject
	public GetUserReportsHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(GetUserReports cmd, User user)
			throws CommandException {

		Query query = em
				.createQuery(
						"select r from ReportDefinition r where r.owner in (:owner)")
				.setParameter("owner", user);

		List<ReportDefinition> results = query.getResultList();

		List<ReportDefinitionDTO> dtos = new ArrayList<ReportDefinitionDTO>();

		for (ReportDefinition template : results) {

			ReportDefinitionDTO dto = new ReportDefinitionDTO();
			dto.setId(template.getId());
			dto.setOwnerName(template.getOwner().getName());
			dto.setAmOwner(template.getOwner().getId() == user.getId());
			dto.setTitle(template.getTitle());
			dto.setFrequency(template.getFrequency());
			dto.setDay(template.getDay());
			dto.setDescription(template.getDescription());
			dto.setEditAllowed(dto.getAmOwner());

			dto.setSubscribed(true);
			
			dtos.add(dto);
		}
		return new ReportTemplateResult(dtos);
	}

}
