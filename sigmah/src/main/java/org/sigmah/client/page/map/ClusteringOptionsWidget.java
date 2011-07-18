package org.sigmah.client.page.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;

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
	private List<CountryDTO> countries = new ArrayList<CountryDTO>();
	private Dispatcher service;
	private HorizontalPanel panelEnclosingAdminLevel = new HorizontalPanel();

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
		if (countries != null) {
			// Show name of country with related adminlevels as option for the user
			for (CountryDTO country : countries) {
				List<AdminLevelDTO> adminLevels = country.getAdminLevels();
	
				// Get a container
				HorizontalPanel panel = new HorizontalPanel();
				
				// Show the countryname using a label
				LabelField label = new LabelField();
				label.setText(country.getName());
				panel.add(label);
				
				if (adminLevels.size() > 0) { // adminlevels found, add them as option
					// Show a combobox with available adminlevels for the country
					ListStore<AdminLevelDTO> adminLevelStore = new ListStore<AdminLevelDTO>();
					adminLevelStore.add(adminLevels);
					final ComboBox<AdminLevelDTO> combobox = new ComboBox<AdminLevelDTO>();
					combobox.setStore(adminLevelStore);
					combobox.setDisplayField("name");
					combobox.setForceSelection(true);
					combobox.addListener(Events.TriggerClick, new Listener<FieldEvent>() {
						@Override
						public void handleEvent(FieldEvent be) {
							combobox.setAllQuery("");
						}
					});
					combobox.addListener(Events.Select, new Listener<FieldEvent>() {
						@Override
						public void handleEvent(FieldEvent be) {
							//adminLevelClustering.
						}
					});
					
					combobox.setEditable(false);
					panel.add(combobox);
	
					// Keep a reference to the adminlevel by country
					pickedAdminLevelsByCountry.put(country, adminLevels.get(0));
				} else { // No adminlevels defined for given country
					LabelField labelUnavailable = new LabelField();
					labelUnavailable.setText("[Unavailable]");
					panel.add(labelUnavailable);
				}

				panelAdministrativeLevelOptions.add(panel);
			}
		} else { // No countries found		getCountries();

			LabelField labelNoCountries = new LabelField("[Unavailable]");
			panelAdministrativeLevelOptions.add(labelNoCountries);
		}
		
		HorizontalPanel panelMargin = new HorizontalPanel();
		panelMargin.setWidth("3em");
		panelEnclosingAdminLevel.add(panelMargin);
		panelEnclosingAdminLevel.add(panelAdministrativeLevelOptions);
		
		setEnabledOnSelectAdminLevel();
		setAdminLevelEnabledOrDisabled();
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

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Clustering> handler) {
		return null;
	}

	@Override
	public Clustering getValue() {
		return selectedClustering;
	}

	@Override
	public void setValue(Clustering value) {
		selectedClustering = value;
		ValueChangeEvent.fire(this, value);
	}

	@Override
	public void setValue(Clustering value, boolean fireEvents) {
		
	}
}