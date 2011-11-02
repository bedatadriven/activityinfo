/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;


import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.i18n.LocaleHelper;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.PivotTableReportElement;

import com.google.inject.Inject;

public class PivotTableGenerator extends PivotGenerator<PivotTableReportElement> {

    @Inject
    public PivotTableGenerator(DispatcherSync dispatcher) {
        super(dispatcher);
    }

    @Override
    public void generate(User user, PivotTableReportElement element, Filter inheritedFilter,
                         DateRange dateRange) {

        Filter filter = GeneratorUtils.resolveElementFilter(element, dateRange);
        Filter effectiveFilter = inheritedFilter == null ? filter : new Filter(inheritedFilter, filter);

        PivotTableData data = generateData(user.getId(), LocaleHelper.getLocaleObject(user), element, effectiveFilter,
                element.getRowDimensions(), element.getColumnDimensions());
        
        
        PivotContent content = new PivotContent();
        content.setEffectiveFilter(effectiveFilter);
        content.setFilterDescriptions(
                generateFilterDescriptions(
                        filter, element.allDimensionTypes(), user));
		content.setData(data);
        element.setContent(content);
    }
}
