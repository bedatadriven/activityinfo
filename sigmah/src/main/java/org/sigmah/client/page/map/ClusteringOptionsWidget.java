package org.sigmah.client.page.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.domain.AdminLevel;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;

/*
 * Shows a list of options to aggregate markers on the map
 */
public class ClusteringOptionsWidget extends LayoutContainer {

	// Aggregation of elements on the map
	private RadioGroup radiogroupAggregation = new RadioGroup();
	private Radio radioAdminLevelAggr = new Radio();
	private Radio radioAutomaticAggr = new Radio();
	private Radio radioNoAggr = new Radio();
	
	// Administrative level clustering
	private VerticalPanel panelAdministrativeLevelOptions = new VerticalPanel();
	private Map<CountryDTO, AdminLevelDTO> pickedAdminLevelsByCountry = new HashMap<CountryDTO, AdminLevelDTO>();
	private List<CountryDTO> countries = new ArrayList<CountryDTO>();
	private Dispatcher service;

	public ClusteringOptionsWidget(Dispatcher service) {
		super();
		
		this.service = service;
		
		setLayout(new RowLayout(Orientation.VERTICAL));
		
		createOptions();
		getCountries();
		
		// By default, no clustering is used for a layer
		radioNoAggr.setValue(true);
	}

	private void createOptions() {
		radioAdminLevelAggr.setBoxLabel(I18N.CONSTANTS.administrativeLevel());
		radioAutomaticAggr.setBoxLabel(I18N.CONSTANTS.automatic());
		radioNoAggr.setBoxLabel(I18N.CONSTANTS.none());

		radiogroupAggregation.add(radioAdminLevelAggr);
		radiogroupAggregation.add(radioAutomaticAggr);
		radiogroupAggregation.add(radioNoAggr);
		
		add(radioAdminLevelAggr);
		add(panelAdministrativeLevelOptions);
		add(radioAutomaticAggr);
		add(radioNoAggr);
	}
	
	private void createAdminLevelOptions() {
		if (countries != null) {
			// Show name of country with related adminlevels as option for the user
			for (CountryDTO country : countries) {
				List<AdminLevelDTO> adminLevels = country.getAdminLevels();
	
				// Get a container
				HorizontalPanel panel = new HorizontalPanel();
				
				// Show the countryname using a label
				Label label = new Label();
				label.setText(country.getName());
				panel.add(label);
				
				if (adminLevels.size() > 0) { // adminlevels found, add them as option
					// Show a combobox with available adminlevels for the country
					ListStore<AdminLevelDTO> adminLevelStore = new ListStore<AdminLevelDTO>();
					adminLevelStore.add(adminLevels);
					ComboBox<AdminLevelDTO> combobox = new ComboBox<AdminLevelDTO>();
					combobox.setStore(adminLevelStore);
					combobox.setPropertyEditor(new ListModelPropertyEditor<AdminLevelDTO>(){
						public String getStringValue(AdminLevelDTO value) {
							return value.getName();
						}
					});
					
					combobox.setEditable(false);
					panel.add(combobox);
	
					// Keep a reference to the adminlevel by country
					pickedAdminLevelsByCountry.put(country, adminLevels.get(0));
				} else { // No adminlevels defined for given country
					Label labelUnavailable = new Label();
					labelUnavailable.setText("[Unavailable]");
					panel.add(labelUnavailable);
				}

				panelAdministrativeLevelOptions.add(panel);
			}
		} else { // No countries found		getCountries();

			Label labelNoCountries = new Label("[Unavailable]");
			panelAdministrativeLevelOptions.add(labelNoCountries);
		}
	}

	private void getCountries() {
		service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				countries = result.getCountries();
				createAdminLevelOptions();
			}
		});
	}

	public Clustering getSelectedClustering() {
		Radio selectedRadio = radiogroupAggregation.getValue();
		if (selectedRadio.equals(radioNoAggr)) {
			return new NoClustering(); 
		}
		if (selectedRadio.equals(radioAdminLevelAggr)) {
			return new AdministrativeLevelClustering(); 
		}
		if (selectedRadio.equals(radioAutomaticAggr)) {
			return new AutomaticClustering(); 
		}

		return null;
	}
}