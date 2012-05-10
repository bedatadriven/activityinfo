package org.activityinfo.client.report.editor.chart;

import java.util.Collections;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.report.editor.DimensionStoreFactory;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement.Type;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class ChartPivotBar extends ToolBar implements HasReportElement<PivotChartReportElement> {
	
	private final ReportEventHelper events;
	
	private PivotChartReportElement model;
	
	protected ComboBox<Dimension> categoryCombo;
	protected ComboBox<Dimension> legendCombo;

	protected LabelToolItem categoryLabel;
	protected LabelToolItem legendLabel;
	
	public ChartPivotBar(EventBus eventBus, Dispatcher dispatcher) {
		this.events = new ReportEventHelper(eventBus, this);
		ListStore<Dimension> store = DimensionStoreFactory.create(dispatcher);
		
		categoryLabel = new LabelToolItem();
		updateCategoryComboLabel(I18N.CONSTANTS.horizontalAxis());
		add(categoryLabel);

		categoryCombo = new ComboBox<Dimension>();
		categoryCombo.setForceSelection(true);
		categoryCombo.setEditable(false);
		categoryCombo.setStore(store);
		categoryCombo.setDisplayField("caption");
		categoryCombo.setValue(new DateDimension(DateUnit.YEAR));
		categoryCombo.addSelectionChangedListener(new SelectionChangedListener<Dimension>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<Dimension> se) {
				model.setCategoryDimension(se.getSelectedItem());
				events.fireChange();
			}
		});
		add(categoryCombo);

		add(new FillToolItem());

		legendLabel = new LabelToolItem();
		updateLegendComboLabel(I18N.CONSTANTS.bars());
		add(legendLabel);

		legendCombo = new ComboBox<Dimension>();
		legendCombo.setForceSelection(true);
		legendCombo.setEditable(false);
		legendCombo.setStore(store);
		legendCombo.setDisplayField("caption");
		legendCombo.addSelectionChangedListener(new SelectionChangedListener<Dimension>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<Dimension> se) {
				model.setSeriesDimension(se.getSelectedItem());
				events.fireChange();
			}
		});
		add(legendCombo);
		
		events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				updateLabels();
				updateCombos();
			}
		});
		
		store.addStoreListener(new StoreListener<Dimension>() {
			@Override
			public void storeDataChanged(StoreEvent<Dimension> se) {
				updateCombos();
			}
		});
		
		store.getLoader().load();
	}
	
	@Override
	public void bind(PivotChartReportElement model) {
		this.model = model;
		updateCombos();
		updateLabels();
	}
	
	public PivotChartReportElement getModel() {
		return model;
	}

	protected void updateCombos() {
		updateCombo(categoryCombo, model.getCategoryDimensions());
		updateCombo(legendCombo, model.getSeriesDimension());
	}
	
	protected void updateLabels() {
		Type type = model.getType();
		if (type == Type.ClusteredBar || type == Type.Bar) {
			updateCategoryComboLabel(I18N.CONSTANTS.horizontalAxis());
			updateLegendComboLabel(I18N.CONSTANTS.bars());
		} else if (type == Type.Line) {
			updateCategoryComboLabel(I18N.CONSTANTS.horizontalAxis());
			updateLegendComboLabel(I18N.CONSTANTS.lines());
		} else if (type == Type.Pie) {
			updateCategoryComboLabel(I18N.CONSTANTS.slices());
		}
		legendCombo.setEnabled(type != Type.Pie);
		legendLabel.setEnabled(type != Type.Pie);
	}

	protected void updateCategoryComboLabel(String label) {
		categoryLabel.setLabel(label);
	}

	protected void updateLegendComboLabel(String label) {
		legendLabel.setLabel(label);
	}
	
	private void updateCombo(ComboBox<Dimension> comboBox, List<Dimension> selection) {
		if(selection.isEmpty()) {
			comboBox.setValue(null);
		} else {
			// we have to look up the dimension in the store because 
			// otherwise we don't have a label
			for(Dimension dim : comboBox.getStore().getModels()) {
				if(dim.equals(selection.get(0))) {
					comboBox.setValue(dim);
					return;
				}
			}
		}
	}

	@Override
	public void disconnect() {
		events.disconnect();
	}

}
