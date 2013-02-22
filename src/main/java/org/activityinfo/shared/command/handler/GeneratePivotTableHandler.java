package org.activityinfo.shared.command.handler;

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

import org.activityinfo.shared.command.GeneratePivotTable;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.handler.pivot.PivotTableDataBuilder;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GeneratePivotTableHandler implements
    CommandHandlerAsync<GeneratePivotTable, PivotContent> {

    @Override
    public void execute(final GeneratePivotTable command,
        ExecutionContext context,
        final AsyncCallback<PivotContent> callback) {

        context.execute(new PivotSites(command.getModel().allDimensions(),
            command.getModel().getFilter()),
            new AsyncCallback<PivotSites.PivotResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(PivotResult result) {
                    callback.onSuccess(buildResult(command.getModel(), result));
                }
            });
    }

    protected PivotContent buildResult(PivotTableReportElement model,
        PivotResult result) {

        PivotTableDataBuilder builder = new PivotTableDataBuilder();
        PivotTableData data = builder.build(model, model.getRowDimensions(),
            model.getColumnDimensions(), result.getBuckets());

        PivotContent content = new PivotContent();
        content.setEffectiveFilter(model.getFilter());
        content.setData(data);

        return content;
    }
}
