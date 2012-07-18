package org.activityinfo.client.report.editor.chart;

import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.report.editor.pivotTable.DimensionModel;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement.Type;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;

public class DimensionComboBoxSet implements HasReportElement<PivotChartReportElement> {

	private final ReportEventHelper events;
	private final DimensionProxy proxy;
	private final ListStore<DimensionModel> store;
	private final ListLoader<ListLoadResult<DimensionModel>> loader;
	private final LabelToolItem categoryLabel;
	private final LabelToolItem seriesLabel;
	private final DimensionCombo categoryCombo;
	private final DimensionCombo seriesCombo;

	private PivotChartReportElement model;
	
	public DimensionComboBoxSet(EventBus eventBus, Dispatcher dispatcher) {
		this.proxy = new DimensionProxy(dispatcher);
		this.loader = new BaseListLoader<ListLoadResult<DimensionModel>>(proxy);
		this.store = new ListStore<DimensionModel>(loader);
		this.categoryCombo = new DimensionCombo(store, new SelectionChangedListener<DimensionModel>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<DimensionModel> se) {
				model.setCategoryDimension(se.getSelectedItem().getDimension());
				events.fireChange();
			}
		});
		this.seriesCombo = new DimensionCombo(store, new SelectionChangedListener<DimensionModel>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<DimensionModel> se) {
				model.setSeriesDimension(se.getSelectedItem().getDimension());
				events.fireChange();
			}
		});		
		this.categoryLabel = new LabelToolItem();
		this.seriesLabel = new LabelToolItem();
		this.events = new ReportEventHelper(eventBus, this);
		this.events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				updateSelection();
			}
		});
	}


	@Override
	public void bind(PivotChartReportElement model) {
		this.model = model;
		this.proxy.setModel(model);
		updateSelection();
	}

	@Override
	public PivotChartReportElement getModel() {
		return this.model;
	}

	@Override
	public void disconnect() {
		events.disconnect();
	}
	
	private void updateSelection() {
		loader.load();
		seriesCombo.setValue(firstOrNull(model.getSeriesDimension()));
		categoryCombo.setValue(firstOrNull(model.getCategoryDimensions()));
		updateLabels();
	}
	
	private void updateLabels() {
		Type type = model.getType();
		if (type == Type.ClusteredBar || type == Type.Bar) {
			categoryLabel.setLabel(I18N.CONSTANTS.horizontalAxis());
			seriesLabel.setLabel(I18N.CONSTANTS.bars());
		} else if (type == Type.Line) {
			categoryLabel.setLabel(I18N.CONSTANTS.horizontalAxis());
			seriesLabel.setLabel(I18N.CONSTANTS.lines());
		} else if (type == Type.Pie) {
			categoryLabel.setLabel(I18N.CONSTANTS.slices());
		}
		seriesCombo.setEnabled(type != Type.Pie);
		seriesCombo.setEnabled(type != Type.Pie);
	}

	private Dimension firstOrNull(List<Dimension> dimensions) {
		if(dimensions.isEmpty()) {
			return null;
		} else {
			return dimensions.get(0);
		}
	}

	public LabelToolItem getCategoryLabel() {
		return categoryLabel;
	}

	public LabelToolItem getSeriesLabel() {
		return seriesLabel;
	}

	public DimensionCombo getCategoryCombo() {
		return categoryCombo;
	}

	public DimensionCombo getSeriesCombo() {
		return seriesCombo;
	}

	private static class DimensionCombo extends ComboBox<DimensionModel> {
		private Dimension dimension;
		
		public DimensionCombo(ListStore<DimensionModel> store, SelectionChangedListener<DimensionModel> changeListener) {
			this.store = store;
			setDisplayField("name");
			setEditable(false);
			setForceSelection(true);
			setTriggerAction(TriggerAction.ALL);
			addSelectionChangedListener(changeListener);
			
			store.addStoreListener(new StoreListener<DimensionModel>() {

				@Override
				public void storeDataChanged(StoreEvent<DimensionModel> se) {
					tryUpdateSelection();
				}
			});
		}
		
		public void setValue(Dimension dimension) {
			this.dimension = dimension;
			tryUpdateSelection();
		}
		
		public void tryUpdateSelection() {
			/// try to update the combo box selection based on the current value
			/// we may need to wait until we receive the list of dimensions from the server
			/// because the model carries only the id, not the descriptive label
			for(DimensionModel model : getStore().getModels()) {
				if(model.getDimension().equals(dimension)) {
					setValue(model);
					return;
				}
			}
			setValue((DimensionModel)null);
		}
	}
	

}
