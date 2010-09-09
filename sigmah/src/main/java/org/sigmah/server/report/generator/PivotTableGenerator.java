/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;


import com.google.inject.Inject;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.server.util.LocaleHelper;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.Filter;
import org.sigmah.shared.report.model.PivotTableElement;

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

        content.setData(generateData(user.getId(), LocaleHelper.getLocaleObject(user), element, effectiveFilter,
                element.getRowDimensions(), element.getColumnDimensions()));

        element.setContent(content);
    }


}
