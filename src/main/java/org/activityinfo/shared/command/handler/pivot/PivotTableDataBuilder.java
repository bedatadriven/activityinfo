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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.handler.pivot.order.CategoryComparator;
import org.activityinfo.shared.command.handler.pivot.order.DefinedCategoryComparator;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.PivotReportElement;

public class PivotTableDataBuilder {


	public PivotTableData build(PivotReportElement<?> element, List<Dimension> rowDims,
			List<Dimension> colDims, List<Bucket> buckets) {
		
		PivotTableData table = new PivotTableData();
        Map<Dimension, Comparator<PivotTableData.Axis>> comparators =
                createComparators(element.allDimensions());

        for (Bucket bucket : buckets) {

            PivotTableData.Axis column = colDims.isEmpty() ? table.getRootColumn() :
                    find(table.getRootColumn(), colDims.iterator(), comparators, bucket);
            PivotTableData.Axis row = rowDims.isEmpty() ? table.getRootRow() :
                    find(table.getRootRow(), rowDims.iterator(), comparators, bucket);

            row.setValue(column, bucket.doubleValue());
        }
        return table;
	}

    protected Map<Dimension, Comparator<PivotTableData.Axis>> createComparators(Set<Dimension> dimensions) {
        Map<Dimension, Comparator<PivotTableData.Axis>> map =
                new HashMap<Dimension, Comparator<PivotTableData.Axis>>();

        for (Dimension dimension : dimensions) {
            if (dimension.isOrderDefined()) {
                map.put(dimension, new DefinedCategoryComparator(dimension.getOrdering()));
            } else {
                map.put(dimension, new CategoryComparator());
            }
        }
        return map;
    }

    protected PivotTableData.Axis find(PivotTableData.Axis axis, Iterator<Dimension> dimensionIterator,
                                       Map<Dimension, Comparator<PivotTableData.Axis>> comparators,
                                       Bucket result) {

        Dimension childDimension = dimensionIterator.next();
        DimensionCategory category = result.getCategory(childDimension);
        PivotTableData.Axis child = null;

        child = axis.getChild(category);
        if (child == null) {

            String categoryLabel;
            if(category == null) {
            	categoryLabel = I18N.CONSTANTS.emptyDimensionCategory();
            } else {
            	categoryLabel = childDimension.getLabel(category);
	            if (categoryLabel == null) {
	                categoryLabel = category.getLabel();
	            }
            }
           
            child = axis.addChild(childDimension,
                    result.getCategory(childDimension),
                    categoryLabel,
                    comparators.get(childDimension));

        }
        if (dimensionIterator.hasNext()) {
            return find(child, dimensionIterator, comparators, result);
        } else {
            return child;
        }
    }
}
