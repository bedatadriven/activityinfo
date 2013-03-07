package org.activityinfo.client.report.editor.chart;

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
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.report.editor.pivotTable.DimensionModel;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotChartReportElement;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DimensionProxy extends RpcProxy<ListLoadResult<DimensionModel>> {

    private PivotChartReportElement model = new PivotChartReportElement();
    private Dispatcher dispatcher;

    public DimensionProxy(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void setModel(PivotChartReportElement model) {
        this.model = model;
    }

    @Override
    protected void load(Object loadConfig,
        final AsyncCallback<ListLoadResult<DimensionModel>> callback) {

        final List<DimensionModel> list = Lists.newArrayList();
        list.add(new DimensionModel(DimensionType.Indicator, I18N.CONSTANTS
            .indicator()));
        list.add(new DimensionModel(DimensionType.Partner, I18N.CONSTANTS
            .partner()));
        list.add(new DimensionModel(DimensionType.Project, I18N.CONSTANTS
            .project()));
        list.add(new DimensionModel(DimensionType.Target, I18N.CONSTANTS
            .realizedOrTargeted()));

        list.add(new DimensionModel(DateUnit.YEAR));
        list.add(new DimensionModel(DateUnit.QUARTER));
        list.add(new DimensionModel(DateUnit.MONTH));
        list.add(new DimensionModel(DateUnit.WEEK_MON));

        if (model.getIndicators().isEmpty()) {
            callback.onSuccess(new BaseListLoadResult<DimensionModel>(list));
        } else {
            dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(SchemaDTO schema) {
                    addGeographicDimensions(list, schema);
                    list.addAll(DimensionModel.attributeGroupModels(schema, model.getIndicators()));
                    callback.onSuccess(new BaseListLoadResult<DimensionModel>(list));
                }
            });
        }
    }

    private void addGeographicDimensions(
        final List<DimensionModel> list, SchemaDTO schema) {
        Set<CountryDTO> countries = schema.getCountriesForIndicators(model
            .getIndicators());

        if (countries.size() == 1) {
            CountryDTO country = countries.iterator().next();
            for (AdminLevelDTO level : country.getAdminLevels()) {
                list.add(new DimensionModel(level));
            }
        }
    }

   
}
