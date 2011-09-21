package org.sigmah.client.page.entry.editor;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.widget.CoordinateField;
import org.sigmah.client.page.common.widget.CoordinateField.Axis;
import org.sigmah.shared.dto.LocationDTO2;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

public class NewLocationFieldSet extends FieldSet {
	public interface NewLocationPresenter {
		public void onAdd(LocationDTO2 lcoation);
	}
	
	private AsyncMonitor monitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	
	public AsyncMonitor getMonitor() {
		return monitor;
	}

	public NewLocationFieldSet(final NewLocationPresenter presenter) {
		setHeading("Add new location");
		FormPanel form = new FormPanel();
		form.setLabelAlign(LabelAlign.TOP);
		form.setHeaderVisible(false);
		
		TextField<String> labelName = new TextField<String>();
		labelName.setFieldLabel(I18N.CONSTANTS.locationDetails());
		form.add(labelName);
		
		TextField<String> labelAxe = new TextField<String>();
		labelAxe.setFieldLabel(I18N.CONSTANTS.axe());
		form.add(labelAxe);
		
		final CoordinateField longField = new CoordinateField(Axis.LONGITUDE);
		longField.setFieldLabel(I18N.CONSTANTS.longitude());
		form.add(longField);

		final CoordinateField latField = new CoordinateField(Axis.LATITUDE);
		latField.setFieldLabel(I18N.CONSTANTS.latitude());
		form.add(latField);
		
		Image imageDragger = IconImageBundle.ICONS.marker().createImage();
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("16px");
		panel.setHeight("16px");
//		panel.add(imageDragger);

//	    DragSource source = new DragSource(panel);
//	    DropTarget target = new DropTarget(map);
//	    target.setFeedback(Feedback.INSERT);
		
//		LabelField labelDragMeOnMap = new LabelField("Drag on map to add");
		
		Button buttonAddLocation = new Button();
		buttonAddLocation.setText("Add location");
		buttonAddLocation.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (latField.isValid() && longField.isValid()) {
					LocationDTO2 location = new LocationDTO2();
					location.setLongitude(longField.getValue());
					location.setLatitude(latField.getValue());
					location.setName("");
					location.setAxe("");
					presenter.onAdd(location);
				}
			}
		});
		form.add(buttonAddLocation);
//		add(labelDragMeOnMap);
		add(form);
	}

}
