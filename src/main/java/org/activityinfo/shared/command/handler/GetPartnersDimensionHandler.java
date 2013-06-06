package org.activityinfo.shared.command.handler;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.GetPartnersDimension;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetPartnersDimensionHandler implements
    CommandHandlerAsync<GetPartnersDimension, PartnerResult> {

    @Override
    public void execute(GetPartnersDimension cmd, ExecutionContext context,
        final AsyncCallback<PartnerResult> callback) {

        final Dimension dimension = new Dimension(DimensionType.Partner);

        PivotSites query = new PivotSites();
        query.setFilter(cmd.getFilter());
        query.setDimensions(dimension);
        query.setValueType(ValueType.DIMENSION);
        context.execute(query, new AsyncCallback<PivotSites.PivotResult>() {

            @Override
            public void onSuccess(PivotSites.PivotResult result) {

                Set<PartnerDTO> partners = new HashSet<PartnerDTO>();

                for (Bucket bucket : result.getBuckets()) {
                    EntityCategory category = (EntityCategory) bucket
                        .getCategory(dimension);
                    if (category == null) {
                        Log.debug("Partner is null: " + bucket.toString());
                    } else {
                        PartnerDTO partner = new PartnerDTO();
                        partner.setId(category.getId());
                        partner.setName(category.getLabel());
                        partners.add(partner);
                    }
                }

                // sort partners by name (fallback on id)
                List<PartnerDTO> list = new ArrayList<PartnerDTO>(partners);
                Collections.sort(list, new Comparator<PartnerDTO>() {
                    @Override
                    public int compare(PartnerDTO p1, PartnerDTO p2) {
                        int result = p1.getName().compareToIgnoreCase(p2.getName());
                        if (result != 0) {
                            return result;
                        } else {
                            return ((Integer) p1.getId()).compareTo(p2.getId());
                        }
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
