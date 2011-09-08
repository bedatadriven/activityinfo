package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sigmah.shared.command.GetPartnersWithSites;
import org.sigmah.shared.command.result.PartnerResult;
import org.sigmah.shared.dao.pivot.Bucket;
import org.sigmah.shared.dao.pivot.PivotDAOAsync;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class GetPartnersWithSitesHandler implements CommandHandlerAsync<GetPartnersWithSites, PartnerResult> {

	private final PivotDAOAsync pivotDAO;

	@Inject
	public GetPartnersWithSitesHandler(PivotDAOAsync pivotDAO) {
		super();
		this.pivotDAO = pivotDAO;
	}

	@Override
	public void execute(GetPartnersWithSites cmd, ExecutionContext context, final AsyncCallback<PartnerResult> callback) {

		final Dimension dimension = new Dimension(DimensionType.Partner);
		pivotDAO.aggregate(context.getTransaction(),
				Collections.singleton(dimension), cmd.getFilter(), context.getUser().getId(), new AsyncCallback<List<Bucket>>() {

			@Override
			public void onSuccess(List<Bucket> buckets) {

				Set<PartnerDTO> partners = new HashSet<PartnerDTO>();

				for(Bucket bucket : buckets) {
					EntityCategory category = (EntityCategory)bucket.getCategory(dimension);
					PartnerDTO partner = new PartnerDTO();
					partner.setId(category.getId());
					partner.setName(category.getLabel());

					partners.add(partner);
				}

				List<PartnerDTO> list = new ArrayList<PartnerDTO>(partners);
				Collections.sort(list, new Comparator<PartnerDTO>() {

					@Override
					public int compare(PartnerDTO p1, PartnerDTO p2) {
						return p1.getName().compareToIgnoreCase(p2.getName());
					}
				});
				callback.onSuccess(new PartnerResult(list));
			}

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
}
