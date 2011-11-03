/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import java.util.List;
import java.util.Locale;

import org.sigmah.server.command.DispatcherSync;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.PivotSites;
import org.sigmah.shared.command.PivotSites.PivotResult;
import org.sigmah.shared.command.handler.pivot.PivotTableDataBuilder;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotReportElement;

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

    	PivotResult result = dispatcher.execute(new PivotSites(element.allDimensions(), filter));
    	
    	PivotTableDataBuilder builder = new PivotTableDataBuilder();
        return builder.build(element, rowDims, colDims, result.getBuckets());
    }
}
