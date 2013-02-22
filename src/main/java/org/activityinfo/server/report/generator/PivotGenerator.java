

package org.activityinfo.server.report.generator;

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
