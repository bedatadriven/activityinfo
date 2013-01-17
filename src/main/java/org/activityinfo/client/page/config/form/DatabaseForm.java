/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config.form;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.RemoteComboBox;
import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.command.result.CountryResult;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DatabaseForm extends FormPanel {
    private final FormBinding binding;

    public DatabaseForm(final Dispatcher dispatcher) {
        binding = new FormBinding(this);

		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(I18N.CONSTANTS.name());
		nameField.setAllowBlank(false);
		nameField.setMaxLength(16);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		add(nameField);
		
		TextField<String> fullNameField = new TextField<String>();
		fullNameField.setFieldLabel(I18N.CONSTANTS.description());
		fullNameField.setMaxLength(50);
        binding.addFieldBinding(new FieldBinding(fullNameField, "fullName"));
		add(fullNameField);

        ComboBox<CountryDTO> countryField = new RemoteComboBox<CountryDTO>();
		countryField.setStore(createCountryStore(dispatcher, countryField));
        countryField.setFieldLabel(I18N.CONSTANTS.country());
        countryField.setValueField("id");
        countryField.setDisplayField("name");
        countryField.setAllowBlank(false);
		countryField.setTriggerAction(TriggerAction.ALL);

		binding.addFieldBinding(new FieldBinding(countryField, "country") {
            @Override
            public void updateModel() {
                ((UserDatabaseDTO)model).setCountry((CountryDTO) field
						.getValue());
			}
		});

		add(countryField);
	}

	private static ListStore<CountryDTO> createCountryStore(
			final Dispatcher dispatcher, final Component component) {

        return new ListStore<CountryDTO>(new BaseListLoader<CountryResult>(new DataProxy<CountryResult>(){
            @Override
            public void load(DataReader<CountryResult> countryResultDataReader, Object loadConfig, final AsyncCallback<CountryResult> callback) {
						dispatcher.execute(new GetCountries(),
								new MaskingAsyncMonitor(component,
										I18N.CONSTANTS.loading()), callback);

            }
        }));
    }

    public FormBinding getBinding() {
        return binding;
    }
}
