package org.sigmah.client.page.map.layerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.MapResources;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;

/*
 * Shows a list of options to aggregate markers on the map
 */
public class ClusteringOptionsWidget extends LayoutContainer implements HasValue<Clustering> {
	private Clustering selectedClustering = new NoClustering();
	private AdministrativeLevelClustering adminLevelClustering = new AdministrativeLevelClustering();
	
	// Aggregation of elements on the map
	private RadioGroup radiogroupAggregation = new RadioGroup();
	private Radio radioAdminLevelAggr = new Radio();
	private Radio radioAutomaticAggr = new Radio();
	private Radio radioNoAggr = new Radio();
	 
	// Administrative level clustering
	private VerticalPanel panelAdministrativeLevelOptions = new VerticalPanel();
	private Map<CountryDTO, AdminLevelDTO> pickedAdminLevelsByCountry = new HashMap<CountryDTO, AdminLevelDTO>();
	private Dispatcher service;
	private SchemaDTO schema;
	private HorizontalPanel panelEnclosingAdminLevel = new HorizontalPanel();
	private List<AdminLevelDTO> selectedAdminLevels = new ArrayList<AdminLevelDTO>();
	private Map<CountryDTO, ComboBox<AdminLevelDTO>> comboboxesByCountry = new HashMap<CountryDTO, ComboBox<AdminLevelDTO>>();

	public ClusteringOptionsWidget(Dispatcher service) {
		super();
		
		this.service = service;
		
		initializeComponent();
		
		createOptions();
		getCountries();
		
		// By default, no clustering is used for a layer
		radioNoAggr.setValue(true);
	}

	private void initializeComponent() {
		panelAdministrativeLevelOptions.setAutoWidth(true);
		panelEnclosingAdminLevel.setAutoWidth(true);
	}

	private void createOptions() {
		radioAdminLevelAggr.setBoxLabel(I18N.CONSTANTS.administrativeLevel());
		radioAutomaticAggr.setBoxLabel(I18N.CONSTANTS.automatic());
		radioNoAggr.setBoxLabel(I18N.CONSTANTS.none());
		radiogroupAggregation.setAutoWidth(true);
		
		radiogroupAggregation.add(radioAdminLevelAggr);
		radiogroupAggregation.add(radioAutomaticAggr);
		radiogroupAggregation.add(radioNoAggr);
		
		add(radioAdminLevelAggr);
		add(panelEnclosingAdminLevel);
		add(radioAutomaticAggr);
		add(radioNoAggr);
		
		radiogroupAggregation.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				setValue(getSelectedClustering());
			}
		});
	}
	
	private void createAdminLevelOptions() {
		List<CountryDTO> countries = schema.getCountries();
		if (countries != null) {
			// Show name of country with related adminlevels as option for the user
			for (CountryDTO country : countries) {
				createAdminLevelsByCountry(country);
			}
		} else {
			createNoCountriesFoundUI();
		}
		
		HorizontalPanel panelMargin = new HorizontalPanel();
		panelMargin.setWidth("1em");
		panelEnclosingAdminLevel.add(panelMargin);
		panelEnclosingAdminLevel.add(panelAdministrativeLevelOptions);
		
		setEnabledOnSelectAdminLevel();
		setAdminLevelEnabledOrDisabled();
		
		layout(true);
	}

	private void createNoCountriesFoundUI() {
		LabelField labelNoCountries = new LabelField("[Unavailable]");
		panelAdministrativeLevelOptions.add(labelNoCountries);
		
		layout();
	}

	private void createAdminLevelsByCountry(CountryDTO country) {
		List<AdminLevelDTO> adminLevels = country.getAdminLevels();

		// Get a container
		HorizontalPanel panel = new HorizontalPanel();
		
		// Show the countryname using a label
		panel.add(new LabelField(country.getName()));
		
		if (adminLevels.size() > 0) { 
			final ComboBox<AdminLevelDTO> combobox = createAdminLevelsCombobox(adminLevels);
			panel.add(combobox);

			// Keep a reference to the adminlevel by country
			pickedAdminLevelsByCountry.put(country, adminLevels.get(0));
			comboboxesByCountry.put(country, combobox);
		} else { // No adminlevels defined for given country
			LabelField labelUnavailable = new LabelField();
			labelUnavailable.setText("[Unavailable]");
			panel.add(labelUnavailable);
		}

		panelAdministrativeLevelOptions.add(panel);
	}

	private ComboBox<AdminLevelDTO> createAdminLevelsCombobox(List<AdminLevelDTO> adminLevels) {
		// Show a combobox with available adminlevels for the country
		ListStore<AdminLevelDTO> adminLevelStore = new ListStore<AdminLevelDTO>();
		adminLevelStore.add(adminLevels);
		final ComboBox<AdminLevelDTO> combobox = new ComboBox<AdminLevelDTO>();
		combobox.setStore(adminLevelStore);
		combobox.setDisplayField("name");
		combobox.setForceSelection(true);
		combobox.setTriggerAction(TriggerAction.ALL);
		combobox.setEditable(false);
		combobox.addListener(Events.Select, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				onAdminLevelSelection(be);
				
			}
		});
		
		return combobox;
	}
	
	protected void onAdminLevelSelection(FieldEvent be) {
		adminLevelClustering.getAdminLevels().clear();
		for(ComboBox<AdminLevelDTO> comboBox : comboboxesByCountry.values()) {
			if(comboBox.getValue() != null) {
				adminLevelClustering.getAdminLevels().add(comboBox.getValue().getId());
			}
		}
		setValue(adminLevelClustering);		
	}
	
	/*
	 * Enables/disables the adminlevel choice UI
	 */
	private void setEnabledOnSelectAdminLevel() {
		radioAdminLevelAggr.addListener(Events.Change, new Listener<FieldEvent>(){
			@Override
			public void handleEvent(FieldEvent be) {
				setAdminLevelEnabledOrDisabled();
			}
		});
	}

	private void setAdminLevelEnabledOrDisabled() {
		panelAdministrativeLevelOptions.setEnabled(radioAdminLevelAggr.getValue());
	}

	private void getCountries() {
		service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				createFailedFetchCountriesUI();
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				schema = result;
				createAdminLevelOptions();
			}
		});
	}

	private void createFailedFetchCountriesUI() {
		LabelField labelFailedFetchCountries = new LabelField("Failed to download list of countries from server, option disabled. Sorry!");
		labelFailedFetchCountries.setAutoWidth(true);
		Image imageError = new Image(MapResources.INSTANCE.error());

		panelAdministrativeLevelOptions.add(imageError);
		panelAdministrativeLevelOptions.add(labelFailedFetchCountries);
		radioAdminLevelAggr.setEnabled(false);
		
		layout();
	}
	
	public Clustering getSelectedClustering() {
		Radio selectedRadio = radiogroupAggregation.getValue();
		if (selectedRadio != null) {
			if (selectedRadio.equals(radioNoAggr)) {
				return new NoClustering(); 
			}
			if (selectedRadio.equals(radioAdminLevelAggr)) {
				AdministrativeLevelClustering newAdminClustering = new AdministrativeLevelClustering();
				for (Integer adminLevel : adminLevelClustering.getAdminLevels()) {
					newAdminClustering.getAdminLevels().add(adminLevel);
				}
				return newAdminClustering;
			}
			if (selectedRadio.equals(radioAutomaticAggr)) {
				return new AutomaticClustering(); 
			}
		}

		return null;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Clustering> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Clustering getValue() {
		return selectedClustering;
	}

	@Override
	public void setValue(Clustering value) {
		selectedClustering = value;
		updateRadios();
		ValueChangeEvent.fire(this, value);
	}

	@Override
	public void setValue(Clustering value, boolean fireEvents) {
		selectedClustering = value;
		updateRadios();
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	private void updateRadios() {
		if (selectedClustering instanceof NoClustering) {
			radioNoAggr.setValue(true);
		}
		if (selectedClustering instanceof AutomaticClustering) {
			radioAutomaticAggr.setValue(true);
		}
		if (selectedClustering instanceof AdministrativeLevelClustering) {
			AdministrativeLevelClustering autoClustering = (AdministrativeLevelClustering) selectedClustering;
			radioAdminLevelAggr.setValue(true);
			clearComboboxesSelection();
			selectCorrectAdminLevels(autoClustering);
		}
	}

	/*
	 * Ensure the correct AdminLevels are selected for given AdminClustering instance
	 */
	private void selectCorrectAdminLevels(AdministrativeLevelClustering adminLevelClustering) {
		if (!schema.getCountries().isEmpty()) {
			for (Integer adminLevelId : adminLevelClustering.getAdminLevels()) {
				CountryDTO country = schema.getCountryByAdminLevelId(adminLevelId);
				List<AdminLevelDTO> newSelection = new ArrayList<AdminLevelDTO>();
				newSelection.add(schema.getAdminLevelById(adminLevelId));
				comboboxesByCountry.get(country).setSelection(newSelection);
			}
		}
	}

	/*
	 * Clear selection for all comboboxes containing the AdminLevels per country
	 */
	private void clearComboboxesSelection() {
		for (ComboBox<AdminLevelDTO> comboboxAdminLevel : comboboxesByCountry.values()) {
			comboboxAdminLevel.clearSelections();
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		radiogroupAggregation.setEnabled(enabled);
	}
}