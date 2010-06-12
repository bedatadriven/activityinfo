package org.activityinfo.client.page.common.filter;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.AdminLevelModel;
import org.activityinfo.shared.dto.CountryModel;
import org.activityinfo.shared.dto.Schema;

import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class AdminLevelProxy implements DataProxy {

    private final Dispatcher service;

    public AdminLevelProxy(Dispatcher service) {
        this.service = service;
    }

    public void load(DataReader dataReader, Object loadConfig, final AsyncCallback callback) {

        service.execute(new GetSchema(), null, new AsyncCallback<Schema>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(Schema schema) {
                ArrayList<AdminLevelModel> models = new ArrayList<AdminLevelModel>();
                for(CountryModel country : schema.getCountries()) {
                    models.addAll(country.getAdminLevels());
                }
                callback.onSuccess(models);
            }
        });

    }
}
