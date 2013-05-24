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
import java.util.List;

import org.activityinfo.client.Log;
import org.activityinfo.shared.command.GetAttributeGroupsWithSites;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.command.result.AttributeGroupResult;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetAttributeGroupsWithSitesHandler implements
    CommandHandlerAsync<GetAttributeGroupsWithSites, AttributeGroupResult> {

    private static final Dimension DIMENSION = new Dimension(DimensionType.AttributeGroup);

    @Override
    public void execute(GetAttributeGroupsWithSites cmd, final ExecutionContext context,
        final AsyncCallback<AttributeGroupResult> callback) {

        final PivotSites query = new PivotSites();
        query.setFilter(cmd.getFilter());
        query.setDimensions(DIMENSION);
        query.setValueType(ValueType.TOTAL_SITES);

        context.execute(query, new AsyncCallback<PivotSites.PivotResult>() {
            @Override
            public void onSuccess(PivotSites.PivotResult result) {
                final List<AttributeGroupDTO> list = new ArrayList<AttributeGroupDTO>();

                // populate the resultlist
                for (Bucket bucket : result.getBuckets()) {
                    EntityCategory category = (EntityCategory) bucket.getCategory(DIMENSION);
                    if (category == null) {
                        Log.debug("AttributeGroup is null: " + bucket.toString());
                    } else {
                        AttributeGroupDTO attributeGroup = new AttributeGroupDTO();
                        attributeGroup.setId(category.getId());
                        attributeGroup.setName(category.getLabel());
                        list.add(attributeGroup);
                    }
                }

                // sort the groups in the list by name
                Collections.sort(list, new Comparator<AttributeGroupDTO>() {
                    @Override
                    public int compare(AttributeGroupDTO g1, AttributeGroupDTO g2) {
                        return g1.getName().compareToIgnoreCase(g2.getName());
                    }
                });

                callback.onSuccess(new AttributeGroupResult(list));
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }
}
