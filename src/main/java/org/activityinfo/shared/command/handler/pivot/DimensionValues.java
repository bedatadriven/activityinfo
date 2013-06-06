package org.activityinfo.shared.command.handler.pivot;

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

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

/**
 * Base table for retrieving pivotfilter-data based on other pivot criteria. Examples are attributegroups, partners and
 * daterange.
 */
public class DimensionValues extends BaseTable {

    @Override
    public boolean accept(PivotSites command) {
        return command.getValueType() == ValueType.DIMENSION;
    }

    @Override
    public void setupQuery(PivotSites command, SqlQuery query) {

        query.from(Tables.SITE, "Site");
        query.leftJoin(Tables.ACTIVITY, "Activity").on(
            "Activity.ActivityId = Site.ActivityId");
        query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on(
            "Activity.DatabaseId = UserDatabase.DatabaseId");
        query.leftJoin(Tables.REPORTING_PERIOD, "Period").on(
            "Period.SiteId = Site.SiteId");
        query.leftJoin(Tables.ATTRIBUTE_VALUE, "AttributeValue").on(
            "Site.SiteId = AttributeValue.SiteId");
        query.leftJoin(Tables.ATTRIBUTE, "Attribute").on(
            "AttributeValue.AttributeId = Attribute.AttributeId");
        query.leftJoin(Tables.ATTRIBUTE_GROUP, "AttributeGroup").on(
            "Attribute.AttributeGroupId = AttributeGroup.AttributeGroupId");

        // dummy data
        query.appendColumn("0", ValueFields.COUNT);
        query.appendColumn(Integer.toString(IndicatorDTO.AGGREGATE_SITE_COUNT), ValueFields.AGGREGATION);
    }

    @Override
    public String getDimensionIdColumn(DimensionType type) {
        switch (type) {
        case Partner:
            return "Site.PartnerId";
        case Activity:
            return "Site.ActivityId";
        case Database:
            return "Activity.DatabaseId";
        case Site:
            return "Site.SiteId";
        case Project:
            return "Site.ProjectId";
        case Location:
            return "Site.LocationId";
        case Indicator:
            return "V.IndicatorId";
        case Attribute:
            return "AttributeValue.AttributeId";
        case AttributeGroup:
            return "Attribute.AttributeGroupId";
        }
        throw new UnsupportedOperationException(type.name());
    }

    @Override
    public String getDateCompleteColumn() {
        return "Period.Date2";
    }

    @Override
    public TargetCategory getTargetCategory() {
        return TargetCategory.REALIZED;
    }

    @Override
    public SqlQuery createSqlQuery() {
        return SqlQuery.selectDistinct();
    }

    @Override
    public boolean groupDimColumns() {
        return false;
    }
}
