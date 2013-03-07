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

import java.util.List;
import java.util.Set;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DimensionModel extends BaseModelData {

    private final Dimension dimension;

    public DimensionModel(String name) {
        setName(name);
        this.dimension = null;
    }

    public DimensionModel(Dimension dimension, String name) {
        super();
        this.dimension = dimension;
        setName(name);
    }

    public DimensionModel(DimensionType type, String name) {
        this.dimension = new Dimension(type);
        setName(name);
    }

    public DimensionModel(DateUnit unit) {
        this.dimension = new DateDimension(unit);
        switch (unit) {
        case YEAR:
            setName(I18N.CONSTANTS.year());
            break;
        case QUARTER:
            setName(I18N.CONSTANTS.quarter());
            break;
        case MONTH:
            setName(I18N.CONSTANTS.month());
            break;
        case WEEK_MON:
            setName(I18N.CONSTANTS.weekMon());
            break;
        default:
            throw new IllegalArgumentException(unit.name());
        }
    }

    public DimensionModel(AdminLevelDTO level) {
        this.dimension = new AdminDimension(level.getId());
        setName(level.getName());
    }

    public DimensionModel(AttributeGroupDTO attributeGroup) {
        this.dimension = new AttributeGroupDimension(attributeGroup.getId());
        setName(attributeGroup.getName());
    }

    public String getCaption() {
        return get("name");
    }

    public void setName(String caption) {
        set("name", caption);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public boolean hasDimension() {
        return dimension != null;
    }
    

    public static List<DimensionModel> attributeGroupModels(SchemaDTO schema, Set<Integer> indicators) {
        /*
         * Attribute Groups retain their own identity and ids 
         * by Activity, but once we get to this stage, we treat
         * attribute groups with the same name as the same thing.
         * 
         * This allows user to define attributes across databases
         * and activities through "offline" coordination.
         */
        Set<String> groupsAdded = Sets.newHashSet();
        List<DimensionModel> models = Lists.newArrayList();
        for (UserDatabaseDTO db : schema.getDatabases()) {
            for (ActivityDTO activity : db.getActivities()) {
                if (activity.containsAny(indicators)) {
                    for (AttributeGroupDTO attributeGroup : activity
                        .getAttributeGroups()) {
                        if (!groupsAdded.contains(attributeGroup.getName())) {
                            DimensionModel dimModel = new DimensionModel(
                                attributeGroup);
                            models.add(dimModel);
                            groupsAdded.add(attributeGroup.getName());
                        }
                    }
                }
            }
        }
        return models;
    }

}
