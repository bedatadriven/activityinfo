package org.activityinfo.client.page.report.template;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class PivotTableTemplate extends ReportElementTemplate {

    public PivotTableTemplate(Dispatcher dispatcher) {
        super(dispatcher);

        setName(I18N.CONSTANTS.pivotTables());
        setDescription(I18N.CONSTANTS.pivotTableDescription());
        setImagePath("pivot.png");
    }

    @Override
    public void createElement(AsyncCallback<ReportElement> callback) {
        PivotTableReportElement table = new PivotTableReportElement();
        table.addColDimension(new DateDimension(DateUnit.YEAR));
        table.addColDimension(new DateDimension(DateUnit.MONTH));
        table.addRowDimension(new Dimension(DimensionType.Partner));

        callback.onSuccess(table);
    }

}
