package org.sigmah.client.page.config.form;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.Application;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.widget.RemoteComboBox;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

public class DatabaseForm extends FormPanel {
    private FormBinding binding;

    public DatabaseForm(final Dispatcher dispatcher) {
        binding = new FormBinding(this);

		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(Application.CONSTANTS.name());
		nameField.setAllowBlank(false);
		nameField.setMaxLength(16);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		add(nameField);
		
		TextField<String> fullNameField = new TextField<String>();
		fullNameField.setFieldLabel(Application.CONSTANTS.description());
		fullNameField.setMaxLength(50);
        binding.addFieldBinding(new FieldBinding(fullNameField, "fullName"));
		add(fullNameField);

        ComboBox<CountryDTO> countryField = new RemoteComboBox<CountryDTO>();
        countryField.setStore(createCountryStore(dispatcher));
        countryField.setFieldLabel(Application.CONSTANTS.country());
        countryField.setValueField("id");
        countryField.setDisplayField("name");
        countryField.setAllowBlank(false);
        binding.addFieldBinding( new FieldBinding(countryField, "country") {
            @Override
            public void updateModel() {
                ((UserDatabaseDTO)model).setCountry((CountryDTO) field.getValue());
            }
        });

        add(countryField);
	}

    private static ListStore<CountryDTO> createCountryStore(final Dispatcher dispatcher) {
        return new ListStore<CountryDTO>(new BaseListLoader<CountryResult>(new DataProxy<CountryResult>(){
            @Override
            public void load(DataReader<CountryResult> countryResultDataReader, Object loadConfig, final AsyncCallback<CountryResult> callback) {
                dispatcher.execute(new GetCountries(), null, callback);
            }
        }));
    }

    public FormBinding getBinding() {
        return binding;
    }
}
