

package org.activityinfo.shared.report.content;

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
import java.util.List;

import org.activityinfo.shared.command.Filter;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotContent implements Content {

    public PivotContent() {

    }
    
    private Filter effectiveFilter;
    private List<FilterDescription> filterDescriptions;
    private PivotTableData data;

    public PivotContent(PivotTableData data, ArrayList<FilterDescription> filterDescriptions) {
        this.data = data;
        this.filterDescriptions = filterDescriptions;
    }

    public List<FilterDescription> getFilterDescriptions() {
        return filterDescriptions;
    }

    public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
        this.filterDescriptions = filterDescriptions;
    }

    public PivotTableData getData() {
        return data;
    }

    public void setData(PivotTableData data) {
        this.data = data;
    }

    public Filter getEffectiveFilter() {
        return effectiveFilter;
    }

    public void setEffectiveFilter(Filter effectiveFilter) {
        this.effectiveFilter = effectiveFilter;
    }
}
