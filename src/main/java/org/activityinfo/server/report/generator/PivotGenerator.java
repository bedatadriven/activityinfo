/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator;

import java.util.List;
import java.util.Locale;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.handler.pivot.PivotTableDataBuilder;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.PivotReportElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public abstract class PivotGenerator<T extends PivotReportElement> extends BaseGenerator<T> {

	public PivotGenerator(DispatcherSync dispatcher) {
        super(dispatcher);
    }

    protected PivotTableData generateData(int userId, Locale locale,
                                          T element,
                                          Filter filter,
                                          List<Dimension> rowDims, List<Dimension> colDims) {

    	PivotResult result = getDispatcher().execute(new PivotSites(element.allDimensions(), filter));
    	
    	PivotTableDataBuilder builder = new PivotTableDataBuilder();
        return builder.build(element, rowDims, colDims, result.getBuckets());
    }
}
