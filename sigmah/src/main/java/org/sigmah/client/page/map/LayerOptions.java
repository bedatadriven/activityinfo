package org.sigmah.client.page.map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.MapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class LayerOptions extends FormPanel {
	private MapLayer mapLayer;

	// Visualization of elements on the map
	private FieldSet fieldsetIndicatorOptions = new FieldSet();
	private RadioGroup radiogroupVisualization = new RadioGroup();
	private Radio radioProportionalCircle = new Radio();
	private Radio radioIcon = new Radio();
	private Radio radioPiechart = new Radio();
	private ListView listviewPiechartPies;

	// Aggregation of elements on the map
	private FieldSet fieldsetAggregation = new FieldSet();
	private RadioGroup radiogroupAggregation = new RadioGroup();
	private Radio radioAdminLevelAggr = new Radio();
	private Radio radioAutomaticAggr = new Radio();
	private Radio radioNoAggr = new Radio();

	public LayerOptions() {
		super();
		
		setLayout(new RowLayout(Orientation.VERTICAL));
		
		createVisualization();
		createAggregation();
		
		setHeading(I18N.CONSTANTS.mapLayerOptions() + " " + "[Selected layer name]");
		setFrame(true);
	}

	private void createVisualization() {
		fieldsetIndicatorOptions.setLayout(new RowLayout(Orientation.VERTICAL));
		fieldsetIndicatorOptions.setHeading(I18N.CONSTANTS.indicatorVisualization());
		
		radioProportionalCircle.setBoxLabel(I18N.CONSTANTS.proportionalCircle());
		radioIcon.setBoxLabel(I18N.CONSTANTS.icon());
		radioPiechart.setBoxLabel(I18N.CONSTANTS.pieChart());
		
		radiogroupVisualization.add(radioProportionalCircle);
		radiogroupVisualization.add(radioIcon);
		radiogroupVisualization.add(radioPiechart);
		
		fieldsetIndicatorOptions.add(radioProportionalCircle);
		fieldsetIndicatorOptions.add(radioIcon);
		fieldsetIndicatorOptions.add(radioPiechart);
		
		add(fieldsetIndicatorOptions);
	}

	private void createAggregation() {
		fieldsetAggregation.setLayout(new RowLayout(Orientation.VERTICAL));
		fieldsetAggregation.setHeading(I18N.CONSTANTS.aggregation());
		
		radioAdminLevelAggr.setBoxLabel(I18N.CONSTANTS.administrativeLevel());
		radioAutomaticAggr.setBoxLabel(I18N.CONSTANTS.automatic());
		radioNoAggr.setBoxLabel(I18N.CONSTANTS.none());

		radiogroupAggregation.add(radioAdminLevelAggr);
		radiogroupAggregation.add(radioAutomaticAggr);
		radiogroupAggregation.add(radioNoAggr);
		
		fieldsetAggregation.add(radioAdminLevelAggr);
		fieldsetAggregation.add(radioAutomaticAggr);
		fieldsetAggregation.add(radioNoAggr);
		
		add(fieldsetAggregation);
	}

	public MapLayer getMapLayer() {
		return mapLayer;
	}

	public void setMapLayer(MapLayer mapLayer) {
		this.mapLayer = mapLayer;
		
		updateUI();
	}

	private void updateUI() {
		
	}
}
