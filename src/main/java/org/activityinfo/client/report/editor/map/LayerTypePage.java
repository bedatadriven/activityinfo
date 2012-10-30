package org.activityinfo.client.report.editor.map;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class LayerTypePage extends WizardPage {

	private RadioGroup radioGroup = new RadioGroup();
	private Radio proportionalCircleRadio = new Radio();
	private Radio iconRadio = new Radio();
	private Radio piechartRadio = new Radio();
	private Radio polygonRadio = new Radio();
	
	public LayerTypePage() {
		
		FlowLayout layout = new FlowLayout();
		layout.setMargin(15);
		setLayout(layout);
		
		proportionalCircleRadio.setValue(true);

		proportionalCircleRadio.setBoxLabel(I18N.CONSTANTS.proportionalCircle());
		iconRadio.setBoxLabel(I18N.CONSTANTS.icon());
		piechartRadio.setBoxLabel(I18N.CONSTANTS.pieChart());
		polygonRadio.setBoxLabel(I18N.CONSTANTS.shadedPolygons());
		
		radioGroup.add(piechartRadio);
		radioGroup.add(proportionalCircleRadio);
		radioGroup.add(iconRadio);
		radioGroup.add(polygonRadio);
		
		Text header = new Text(I18N.CONSTANTS.chooseSymbol());
		header.setTagName("h2");
				
		add(header, new FlowData(new Margins(0, 0, 15, 0)));
		
		add(proportionalCircleRadio);
		add(iconRadio);
		add(piechartRadio);
		add(polygonRadio);
		
		
		// Let the user know whether or not he can select multiple indicators for the layer
		// he wants to add to the map
		radioGroup.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				LayerTypePage.this.fireEvent(Events.Change, new BaseEvent(Events.Change));
			}
		});
	}	
	
	public MapLayer newLayer() {
		if(radioGroup.getValue() == proportionalCircleRadio) {
			return new BubbleMapLayer();
		} else if(radioGroup.getValue() == iconRadio) {
			return new IconMapLayer();
		} else if(radioGroup.getValue() == piechartRadio) {
			return new PiechartMapLayer();
		} else if(radioGroup.getValue() == polygonRadio) {
			return new PolygonMapLayer();
		} else {
			throw new IllegalStateException();
		}
	}
}
