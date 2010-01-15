package org.activityinfo.server.report.generator;

import org.activityinfo.server.dao.PivotDAO;
import org.activityinfo.shared.report.content.*;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.PivotElement;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class PivotGenerator<T extends PivotElement> extends BaseGenerator<T> {
    public PivotGenerator(PivotDAO pivotDAO) {
        super(pivotDAO);
    }

    protected PivotTableData generateData(Locale locale, T element,
                                          Filter filter,
                                          List<Dimension> rowDims, List<Dimension> colDims) {


        PivotTableData table = new PivotTableData(rowDims, colDims);


        List<PivotDAO.Bucket> buckets = pivotDAO.aggregate(
                filter,
                element.allDimensions());


        Map<Dimension, Comparator<PivotTableData.Axis>> comparators =
                createComparators(element.allDimensions());

        for (PivotDAO.Bucket bucket : buckets) {

            PivotTableData.Axis column = colDims.isEmpty() ? table.getRootColumn() :
                    find(locale, table.getRootColumn(), colDims.iterator(), comparators, bucket);
            PivotTableData.Axis row = rowDims.isEmpty() ? table.getRootRow() :
                    find(locale, table.getRootRow(), rowDims.iterator(), comparators, bucket);

            row.setValue(column, bucket.doubleValue());
        }

//        table.getRootColumn().complete(new DateAxisCompleter());
//        //table.getRootRow().complete(new DateAxisCompleter());


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

    protected PivotTableData.Axis find(Locale locale, PivotTableData.Axis axis,
                                       Iterator<Dimension> dimensionIterator,
                                       Map<Dimension, Comparator<PivotTableData.Axis>> comparators,
                                       PivotDAO.Bucket result) {

        Dimension childDimension = dimensionIterator.next();
        DimensionCategory category = result.getCategory(childDimension);

        PivotTableData.Axis child = axis.getChild(category);
        if (child == null) {

            String categoryLabel = childDimension.getLabel(category);

            if (categoryLabel == null) {
                if (category instanceof LabeledDimensionCategory)
                    categoryLabel = ((LabeledDimensionCategory) category).getLabel();
                else
                    categoryLabel = renderLabel(locale, childDimension, category);
            }

            child = axis.addChild(childDimension,
                    result.getCategory(childDimension),
                    categoryLabel,
                    comparators.get(childDimension));

        }
        if (dimensionIterator.hasNext()) {
            return find(locale, child, dimensionIterator, comparators, result);
        } else {
            return child;
        }
    }

    private String renderLabel(Locale locale, Dimension childDimension, DimensionCategory category) {
        if (category instanceof YearCategory) {
            return Integer.toString(((YearCategory) category).getYear());

        } else if (category instanceof QuarterCategory) {
            // TODO: i18n
            QuarterCategory quarter = (QuarterCategory) category;
            return Integer.toString(quarter.getYear()) + "T" + quarter.getQuarter();

        } else if (category instanceof MonthCategory) {
            SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
            String[] months = format.getDateFormatSymbols().getShortMonths();

            return months[((MonthCategory) category).getMonth() - 1];
        } else if (category instanceof SimpleCategory) {
            return ((SimpleCategory) category).getLabel();
        }
        return "NOIMPL"; // TODO
    }

    private static class CategoryComparator implements Comparator<PivotTableData.Axis> {

        @Override
        public int compare(PivotTableData.Axis a1, PivotTableData.Axis a2) {
            Comparable c1 = a1.getCategory().getSortKey();
            Comparable c2 = a2.getCategory().getSortKey();

            if (c1 == null && c2 == null) {
                return 0;
            }
            if (c1 == null)
                return -1;
            if (c2 == null)
                return 1;


            return c1.compareTo(c2);
        }
    }

    private static class DefinedCategoryComparator implements Comparator<PivotTableData.Axis> {
        private final Map<DimensionCategory, Integer> orderMap;

        public DefinedCategoryComparator(List<DimensionCategory> order) {
            orderMap = new HashMap<DimensionCategory, Integer>();

            for (int i = 0; i != order.size(); ++i) {
                orderMap.put(order.get(i), i);
            }

        }

        @Override
        public int compare(PivotTableData.Axis a1, PivotTableData.Axis a2) {
            Integer o1 = orderMap.get(a1.getCategory());
            Integer o2 = orderMap.get(a2.getCategory());

            if (o1 == null) {
                o1 = Integer.MAX_VALUE;
            }
            if (o2 == null) {
                o2 = Integer.MAX_VALUE;
            }

            return o1.compareTo(o2);
        }
    }
}
