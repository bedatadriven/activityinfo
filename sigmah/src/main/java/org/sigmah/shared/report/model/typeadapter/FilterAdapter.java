/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.sigmah.shared.report.model.typeadapter;

import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.Filter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class FilterAdapter extends XmlAdapter<
        FilterAdapter.FilterElement,
        Filter> {

    public static class Restriction {
        @XmlAttribute
        public String dimension;
        @XmlElement(name="category")
        public List<String> categories = new ArrayList<String>(0);
    }

    public static class FilterElement {
        @XmlElement(name="restriction")
        public List<Restriction> restrictions = new ArrayList<Restriction>(0);

        @XmlElement
        public DateRange dateRange;
    }

    @Override
    public Filter unmarshal(FilterElement element) throws Exception {
        Filter filter = new Filter();
        filter.setDateRange(element.dateRange);

        for(Restriction r : element.restrictions) {
            for(String s : r.categories) {
                filter.addRestriction(findDimType(r.dimension), Integer.parseInt(s));
            }
        }
        return filter;
    }

    private DimensionType findDimType(String name) {
        String nameLowered = name.toLowerCase();
        for(DimensionType type : DimensionType.values()) {
            if(type.toString().toLowerCase().equals(nameLowered)) {
                return type;
            }
        }
        throw new RuntimeException("No DimensionType could be find to match '" + name + "'");
    }

    @Override
    public FilterElement marshal(Filter filter) throws Exception {
        FilterElement element = new FilterElement();
        element.dateRange = filter.getDateRange();
        for(DimensionType t : filter.getRestrictedDimensions()) {
            Restriction r = new Restriction();
            r.dimension = t.toString().toLowerCase();
            for(Integer id : filter.getRestrictions(t) ) {
                r.categories.add(id.toString());
            }
            element.restrictions.add(r);
        }
        return element;
    }
}
