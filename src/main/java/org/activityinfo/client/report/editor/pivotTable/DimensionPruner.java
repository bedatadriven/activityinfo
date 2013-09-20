package org.activityinfo.client.report.editor.pivotTable;

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

import java.util.Set;
import java.util.logging.Logger;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventBus;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Removes inapplicable dimensions from the model after a user change.
 * 
 * <p>
 * For example, if an attribute dimension related to activity X is selected, but
 * all indicators from Activity are removed, then we need to remove the
 * dimension.
 */
public class DimensionPruner implements
    HasReportElement<PivotTableReportElement> {

    private static final Logger LOGGER = Logger.getLogger(DimensionPruner.class
        .getName());

    private final ReportEventBus reportEventBus;
    private PivotTableReportElement model;
    private Dispatcher dispatcher;

    @Inject
    public DimensionPruner(EventBus eventBus, Dispatcher dispatcher) {
        super();
        this.dispatcher = dispatcher;
        this.reportEventBus = new ReportEventBus(eventBus, this);
        this.reportEventBus.listen(new ReportChangeHandler() {

            @Override
            public void onChanged() {
                DimensionPruner.this.onChanged();
            }
        });
    }

    protected void onChanged() {
        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SchemaDTO result) {
                pruneModel(result);
            }
        });
    }

    private void pruneModel(SchemaDTO schema) {
        Set<ActivityDTO> activityIds = getSelectedActivities(schema);
        Set<AttributeGroupDimension> dimensions = getSelectedAttributes(schema);
        boolean dirty = false;
        for (AttributeGroupDimension dim : dimensions) {
            if (!isApplicable(schema, activityIds, dim)) {
                LOGGER.fine("Removing attribute group "
                    + dim.getAttributeGroupId());
                model.getRowDimensions().remove(dim);
                model.getColumnDimensions().remove(dim);
                dirty = true;
            }
        }
        if (dirty) {
            reportEventBus.fireChange();
        }
    }

    private boolean isApplicable(SchemaDTO schema,
        Set<ActivityDTO> activities, AttributeGroupDimension dim) {

        String attributeName = schema.getAttributeGroupNameSafe(dim.getAttributeGroupId());
        
        for (ActivityDTO activity : activities) {
            if (activity.getAttributeGroupByName(attributeName) != null) {
                return true;
            }
        }
        return false;
    }

    private Set<AttributeGroupDimension> getSelectedAttributes(SchemaDTO schema) {
        Set<AttributeGroupDimension> dimensions = Sets.newHashSet();
        for (Dimension dim : model.allDimensions()) {
            if (dim instanceof AttributeGroupDimension) {
                dimensions.add((AttributeGroupDimension) dim);
            }
        }
        return dimensions;
    }

    private Set<ActivityDTO> getSelectedActivities(SchemaDTO schema) {
        Set<ActivityDTO> activities = Sets.newHashSet();
        Set<Integer> indicatorIds = Sets.newHashSet(
            model.getFilter().getRestrictions(DimensionType.Indicator));
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                for (IndicatorDTO indicator : activity.getIndicators()) {
                    if (indicatorIds.contains(indicator.getId())) {
                        activities.add(activity);
                    }
                }
            }
        }
        return activities;
    }

    @Override
    public void bind(PivotTableReportElement model) {
        this.model = model;
    }

    @Override
    public PivotTableReportElement getModel() {
        return model;
    }

    @Override
    public void disconnect() {
        reportEventBus.disconnect();
    }
}
