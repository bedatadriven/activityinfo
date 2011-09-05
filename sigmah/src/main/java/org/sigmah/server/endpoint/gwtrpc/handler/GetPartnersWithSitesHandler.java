package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.sigmah.server.dao.PivotDAO;
import org.sigmah.server.dao.PivotDAO.Bucket;
import org.sigmah.shared.command.GetPartnersWithSites;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.PartnerResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class GetPartnersWithSitesHandler implements CommandHandler<GetPartnersWithSites> {

	private final PivotDAO pivotDAO;
	
	@Inject
	public GetPartnersWithSitesHandler(PivotDAO pivotDAO) {
		super();
		this.pivotDAO = pivotDAO;
	}

	@Override
	public CommandResult execute(GetPartnersWithSites cmd, User user)
			throws CommandException {
		
		
		Dimension dimension = new Dimension(DimensionType.Partner);
		List<Bucket> buckets = pivotDAO.aggregate(user.getId(), cmd.getFilter(), Collections.singleton(dimension));
		
		Set<PartnerDTO> partners = Sets.newHashSet();
		
		for(Bucket bucket : buckets) {
			EntityCategory category = (EntityCategory)bucket.getCategory(dimension);
			PartnerDTO partner = new PartnerDTO();
			partner.setId(category.getId());
			partner.setName(category.getLabel());
			
			partners.add(partner);
		}
		
		List<PartnerDTO> list = Lists.newArrayList(partners);
		Collections.sort(list, new Comparator<PartnerDTO>() {

			@Override
			public int compare(PartnerDTO p1, PartnerDTO p2) {
				return p1.getName().compareToIgnoreCase(p2.getName());
			}
		});
		
		return new PartnerResult(list);
	}

	
}
