/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.report;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.*;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class DimensionStoreFactory {

    public static ListStore<Dimension> create(final Dispatcher service) {

        DataProxy<ListLoadResult<Dimension>> proxy = new DataProxy<ListLoadResult<Dimension>>() {
            public void load(DataReader<ListLoadResult<Dimension>> reader,
                             Object loadConfig, final AsyncCallback<ListLoadResult<Dimension>> callback) {

                final List<Dimension> list = new ArrayList<Dimension>();

                list.add(createDimension(DimensionType.Database, I18N.CONSTANTS.database()));
                list.add(createDimension(DimensionType.Activity, I18N.CONSTANTS.activity()));
                list.add(createDimension(DimensionType.Indicator, I18N.CONSTANTS.indicators()));

                list.add(createDimension(DateUnit.YEAR, I18N.CONSTANTS.year()));
                list.add(createDimension(DateUnit.QUARTER, I18N.CONSTANTS.quarter()));
                list.add(createDimension(DateUnit.MONTH, I18N.CONSTANTS.month()));

                service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
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
