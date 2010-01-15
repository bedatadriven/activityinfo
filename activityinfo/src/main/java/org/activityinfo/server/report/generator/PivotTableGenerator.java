package org.activityinfo.server.report.generator;


import com.google.inject.Inject;
import org.activityinfo.server.dao.PivotDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.PivotTableElement;

import java.util.Comparator;
import java.util.Map;

public class PivotTableGenerator extends PivotGenerator<PivotTableElement> {

    @Inject
    public PivotTableGenerator(PivotDAO pivotDAO) {
        super(pivotDAO);
    }

    private Map<Dimension, Comparator<PivotTableData.Axis>> comparators;

    @Override
    public void generate(User user, PivotTableElement element, Filter inheritedFilter,
                         DateRange dateRange) {

        Filter filter = resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        PivotContent content = new PivotContent();
        content.setEffectiveFilter(effectiveFilter);
        content.setFilterDescriptions(
                generateFilterDescriptions(
                        filter, element.allDimensionTypes(), user));

        content.setData(generateData(user.getLocaleObject(), element, effectiveFilter,
                element.getRowDimensions(), element.getColumnDimensions()));

        element.setContent(content);
    }


}
