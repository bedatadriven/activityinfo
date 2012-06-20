/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.report.editor;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DimensionStoreFactory {

    public static ListStore<Dimension> create(final Dispatcher service) {

        DataProxy<ListLoadResult<Dimension>> proxy = new DataProxy<ListLoadResult<Dimension>>() {
            public void load(DataReader<ListLoadResult<Dimension>> reader,
                             Object loadConfig, final AsyncCallback<ListLoadResult<Dimension>> callback) {

                final List<Dimension> list = new ArrayList<Dimension>();

                list.add(createDimension(DimensionType.Database, I18N.CONSTANTS.database()));
                list.add(createDimension(DimensionType.Activity, I18N.CONSTANTS.activity()));
                list.add(createDimension(DimensionType.Indicator, I18N.CONSTANTS.indicators()));
                list.add(createDimension(DimensionType.Partner, I18N.CONSTANTS.partners()));

                list.add(createDimension(DateUnit.YEAR, I18N.CONSTANTS.year()));
                list.add(createDimension(DateUnit.QUARTER, I18N.CONSTANTS.quarter()));
                list.add(createDimension(DateUnit.MONTH, I18N.CONSTANTS.month()));

                service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
                    public void onFailure(Throwable caught) {

                    }

                    public void onSuccess(SchemaDTO schema) {

                        for (CountryDTO country : schema.getCountries()) {
                            for (AdminLevelDTO level : country.getAdminLevels()) {
                                AdminDimension dimension = new AdminDimension(level.getId());
                                dimension.set("caption", level.getName());
                                list.add(dimension);
                            }
                        }
                   
                        callback.onSuccess(new BaseListLoadResult(list));
                    }
                });

            }
        };

        BaseListLoader<ListLoadResult<Dimension>> loader = new BaseListLoader<ListLoadResult<Dimension>>(proxy);

        return new ListStore<Dimension>(loader);
    }


    private static Dimension createDimension(DimensionType type, String caption) {
        Dimension dim = new Dimension(type);
        dim.set("caption", caption);
        return dim;
    }

    private static Dimension createDimension(DateUnit unit, String caption) {
        Dimension dim = new DateDimension(unit);
        dim.set("caption", caption);
        return dim;
    }
}
