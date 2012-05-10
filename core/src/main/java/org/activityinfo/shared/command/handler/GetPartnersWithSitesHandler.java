package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetPartnersWithSitesHandler implements CommandHandlerAsync<GetPartnersWithSites, PartnerResult> {

	@Override
	public void execute(GetPartnersWithSites cmd, ExecutionContext context, final AsyncCallback<PartnerResult> callback) {

		final Dimension dimension = new Dimension(DimensionType.Partner);

		context.execute(new PivotSites(Collections.singleton(dimension),  cmd.getFilter()), new AsyncCallback<PivotSites.PivotResult>() {

			@Override
			public void onSuccess(PivotSites.PivotResult result) {

				Set<PartnerDTO> partners = new HashSet<PartnerDTO>();

				for(Bucket bucket : result.getBuckets()) {
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
