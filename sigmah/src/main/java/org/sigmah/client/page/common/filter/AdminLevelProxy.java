package org.sigmah.client.page.common.filter;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.SchemaDTO;

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

        service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(SchemaDTO schema) {
                ArrayList<AdminLevelDTO> models = new ArrayList<AdminLevelDTO>();
                for(CountryDTO country : schema.getCountries()) {
                    models.addAll(country.getAdminLevels());
                }
                callback.onSuccess(models);
            }
        });

    }
}
