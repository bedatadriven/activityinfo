package org.activityinfo.client.page.entry.location;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.LocationTypeDTO;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

/**
 * Dialog that presents a choice to the user to select an existing location 
 * or add a new location.
 */
public class LocationDialog extends Window {

	private Dispatcher dispatcher;
	private final LocationSearchPresenter searchPresenter;
	private final NewLocationPresenter newLocationPresenter;
	
	private Html formHeader;
	private Html addLocationHeader;
	private Html addLocationHelp;
	private Button addLocationButton;
	
	private Button useLocationButton;
	
	
	public interface Callback {
		void onSelected(LocationDTO location, boolean isNew);
	}
	
	private Callback callback;
	
	public LocationDialog(Dispatcher dispatcher, CountryDTO country, LocationTypeDTO locationType) {
		this.dispatcher = dispatcher;
		this.searchPresenter = new LocationSearchPresenter(dispatcher, country, locationType);
		this.newLocationPresenter = new NewLocationPresenter(country);
		
		setHeading(I18N.CONSTANTS.chooseLocation());
		setWidth((int)(com.google.gwt.user.client.Window.getClientWidth() * 0.95));
		setHeight((int)(com.google.gwt.user.client.Window.getClientHeight() * 0.95));
		setLayout(new BorderLayout());
		
		addSearchPanel(locationType);
		addMap();
				
		getButtonBar().setAlignment(HorizontalAlignment.LEFT);
		getButtonBar().add(useLocationButton = new Button(I18N.CONSTANTS.useLocation(), IconImageBundle.ICONS.useLocation(),
				new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				searchPresenter.accept();
			}
		}));
		useLocationButton.disable();
		
		getButtonBar().add(new Button(I18N.CONSTANTS.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		}));
		
		newLocationPresenter.addListener(NewLocationPresenter.ACTIVE_STATE_CHANGED, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if(newLocationPresenter.isActive()) {
					formHeader.setHtml(I18N.CONSTANTS.addLocation());
				} else {
					formHeader.setHtml(I18N.CONSTANTS.searchLocations());
				}
				addLocationHeader.setVisible(!newLocationPresenter.isActive());
				addLocationHelp.setVisible(!newLocationPresenter.isActive());
				addLocationButton.setVisible(!newLocationPresenter.isActive());
				layout();
			}
		});
		
		newLocationPresenter.addAcceptedListener(new Listener<LocationEvent>() {
			
			@Override
			public void handleEvent(LocationEvent event) {
				hide();
				callback.onSelected(event.getLocation(), true);
			}
		});
		
		searchPresenter.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if(searchPresenter.getSelection() == null) {
					useLocationButton.disable();
				} else {
					useLocationButton.enable();
					useLocationButton.setText(I18N.MESSAGES.useLocation(searchPresenter.getSelection().getName()));
				}
			}
		});
		
		searchPresenter.addAcceptListener(new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				hide();
				callback.onSelected(searchPresenter.getSelection(), false);
			}
		});
	}

	private void addSearchPanel(LocationTypeDTO locationType) {
		
		LayoutContainer container = new LayoutContainer();
		container.setLayout(new FlowLayout());
		container.setScrollMode(Scroll.AUTOY);
		container.addStyleName(SiteFormResources.INSTANCE.style().locationDialogPane());
	
		container.add(newHeader(I18N.CONSTANTS.chooseLocation()));
		container.add(newExplanation(I18N.CONSTANTS.chooseLocationDescription()));
		
		container.add(formHeader = newHeader(I18N.CONSTANTS.searchLocations()));
		container.add(new LocationForm(dispatcher, locationType.getId(), searchPresenter, newLocationPresenter));
			
		container.add(newHeader(I18N.CONSTANTS.searchResults()));
		container.add(new SearchListView(searchPresenter));
		container.add(new SearchStatusView(searchPresenter));
		
		container.add(addLocationHeader = newHeader(I18N.CONSTANTS.addLocation()));
		container.add(addLocationHelp = newExplanation(I18N.CONSTANTS.addLocationDescription()));
		container.add(addLocationButton = new Button(I18N.CONSTANTS.newLocation(), IconImageBundle.ICONS.add(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				newLocationPresenter.setActive(true);
			}
		}));
		addLocationButton.addStyleName(SiteFormResources.INSTANCE.style().addLocationButton());

		
		BorderLayoutData layout = new BorderLayoutData(LayoutRegion.WEST);
		layout.setSize(350);
		
		add(container, layout);
	}

	private Html newHeader(String string) {
		Html html = new Html(string);
		html.addStyleName(SiteFormResources.INSTANCE.style().locationDialogHeader());
		return html;
	}

	private Html newExplanation(String string) {
		Html html = new Html(string);
		html.addStyleName(SiteFormResources.INSTANCE.style().locationDialogHelp());
		return html;
	}
	
	private void addMap() {
		LocationMap mapView = new LocationMap(searchPresenter, newLocationPresenter);
		
		add(mapView, new BorderLayoutData(LayoutRegion.CENTER));
	}
	
	public void show(Callback callback) {
		this.callback = callback;
		show();
	}
}
