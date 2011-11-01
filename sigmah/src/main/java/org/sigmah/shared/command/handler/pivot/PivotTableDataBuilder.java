package org.sigmah.shared.command.handler.pivot;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.result.Bucket;
import org.sigmah.shared.report.content.DimensionCategory;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.generator.pivot.CategoryComparator;
import org.sigmah.shared.report.generator.pivot.DefinedCategoryComparator;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotReportElement;

public class PivotTableDataBuilder {


	public PivotTableData build(PivotReportElement<?> element, List<Dimension> rowDims,
			List<Dimension> colDims, List<Bucket> buckets) {
		
		PivotTableData table = new PivotTableData(rowDims, colDims);
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
