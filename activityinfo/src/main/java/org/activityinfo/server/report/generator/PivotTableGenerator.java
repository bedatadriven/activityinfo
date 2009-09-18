package org.activityinfo.server.report.generator;


import java.util.*;

import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.report.content.*;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.PivotTableElement;

import com.google.inject.Inject;

public class PivotTableGenerator extends PivotGenerator<PivotTableElement> {

    @Inject
    public PivotTableGenerator(PivotDAO pivotDAO) {
        super(pivotDAO);
    }

    private Map<Dimension, Comparator<PivotTableData.Axis>> comparators;

    @Override
    public void generate(User user, PivotTableElement element, Filter inheritedFilter,
                         Map<String, Object> parameterValues) {

        Filter filter = ParamFilterHelper.resolve(element.getFilter(), parameterValues);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        PivotContent content = new PivotContent();
        content.setEffectiveFilter(effectiveFilter);
        content.setFilterDescriptions(
                generateFilterDescriptions(
                        filter, element.allDimensionTypes()));

        content.setData(generateData(user.getLocaleObject(), element, effectiveFilter,
                element.getRowDimensions(), element.getColumnDimensions()));

        element.setContent(content);
    }


}
