package org.activityinfo.shared.command.handler.pivot.order;

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

import java.util.Comparator;

import org.activityinfo.shared.report.content.PivotTableData;

public class CategoryComparator implements Comparator<PivotTableData.Axis> {

    @Override
    public int compare(PivotTableData.Axis a1, PivotTableData.Axis a2) {
        Comparable c1 = sortKey(a1);
        Comparable c2 = sortKey(a2);

        if (c1 == null && c2 == null) {
            return 0;
        }
        if (c1 == null) {
            return -1;
        }
        if (c2 == null) {
            return 1;
        }


        return c1.compareTo(c2);
    }

	private Comparable sortKey(PivotTableData.Axis a1) {
		if(a1.getCategory() == null) {
			return null;
		} else {
			return a1.getCategory().getSortKey();
		}
	}
}