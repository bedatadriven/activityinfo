package org.activityinfo.client.page.entry;

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

import org.activityinfo.client.util.date.DateUtilGWTImpl;
import org.activityinfo.shared.report.content.MonthCategory;
import org.activityinfo.shared.report.model.DateRange;

import com.extjs.gxt.ui.client.data.BaseModelData;

final class MonthModel extends BaseModelData {

    private final MonthCategory category;

    public MonthModel(MonthCategory category) {
        this.category = category;
        set("name", category.getLabel());
    }

    public DateRange getDateRange() {
        return DateUtilGWTImpl.INSTANCE.monthRange(category.getYear(),
            category.getMonth());
    }

    public String getKey() {
        return "Y" + category.getYear() + "M" + category.getMonth();
    }

}
