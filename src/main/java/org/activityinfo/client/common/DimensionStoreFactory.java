package org.activityinfo.client.common;

import org.activityinfo.shared.report.model.*;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.CountryModel;
import org.activityinfo.shared.dto.AdminLevelModel;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.Application;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.data.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class DimensionStoreFactory {

    public static ListStore<Dimension> create(final CommandService service) {

        DataProxy<ListLoadResult<Dimension>> proxy = new DataProxy<ListLoadResult<Dimension>>() {
            public void load(DataReader<ListLoadResult<Dimension>> reader,
                             Object loadConfig, final AsyncCallback<ListLoadResult<Dimension>> callback) {

                final List<Dimension> list = new ArrayList<Dimension>();

                list.add(createDimension(DimensionType.Database, Application.CONSTANTS.database()));
                list.add(createDimension(DimensionType.Activity, Application.CONSTANTS.activity()));
                list.add(createDimension(DimensionType.Indicator,  Application.CONSTANTS.indicators()));

                list.add(createDimension(DateUnit.YEAR, Application.CONSTANTS.year()));
                list.add(createDimension(DateUnit.QUARTER, Application.CONSTANTS.quarter()));
                list.add(createDimension(DateUnit.MONTH, Application.CONSTANTS.month()));

                service.execute(new GetSchema(), null, new AsyncCallback<Schema>() {
                    public void onFailure(Throwable caught) {

                    }

                    public void onSuccess(Schema schema) {

                        CountryModel country = schema.getCommonCountry();
                        for(AdminLevelModel level : country.getAdminLevels()) {
                            AdminDimension dimension = new AdminDimension(level.getId());
                            dimension.set("caption", level.getName());
                            list.add(dimension);
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
