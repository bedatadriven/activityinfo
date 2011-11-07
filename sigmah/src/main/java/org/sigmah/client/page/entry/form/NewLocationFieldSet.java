package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.CoordinateField;
import org.sigmah.client.page.common.widget.CoordinateField.Axis;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class NewLocationFieldSet extends LayoutContainer {
	public interface NewLocationPresenter {
		public void onAdd(LocationDTO lcoation);
	}
	
	private AsyncMonitor monitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	private TextField<String> labelName;
	private TextField<String> labelAxe;
	private CoordinateField longField;
	private CoordinateField latField;
	
	public AsyncMonitor getMonitor() {
		return monitor;
	}

	public NewLocationFieldSet(final NewLocationPresenter presenter) {
		setLayout(new FormLayout()); 
		setShadow(false);
		
		labelName = new TextField<String>();
		labelName.setFieldLabel(I18N.CONSTANTS.locationDetails());
		add(labelName);
		
		labelAxe = new TextField<String>();
		labelAxe.setFieldLabel(I18N.CONSTANTS.axe());
		add(labelAxe);
		
		longField = new CoordinateField(Axis.LONGITUDE);
		longField.setFieldLabel(I18N.CONSTANTS.longitude());
		add(longField);
		
		latField = new CoordinateField(Axis.LATITUDE);
		latField.setFieldLabel(I18N.CONSTANTS.latitude());
		add(latField);
		
		Button buttonAddLocation = new Button();
		buttonAddLocation.setText("Add location");
		buttonAddLocation.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (latField.isValid() && longField.isValid()) {
					presenter.onAdd(new LocationDTO()
						.setLongitude(longField.getValue())
						.setLatitude(latField.getValue())
						.setName(labelName.getValue())
						.setAxe(labelAxe.getValue()));
				}			
			}
		});
		add(buttonAddLocation);
	}

}
