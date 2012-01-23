package org.sigmah.client.page.charts;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.table.PivotGridPanel;
import org.sigmah.client.report.DimensionStoreFactory;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.report.content.PivotChartContent;
import org.sigmah.shared.report.model.DateDimension;
import org.sigmah.shared.report.model.DateUnit;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement.Type;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractChart extends LayoutContainer implements
		ActionListener {

	protected final EventBus eventBus;
	protected final Dispatcher service;
	protected ActionToolBar toolBar;

	protected ComboBox<Dimension> categoryCombo;
	protected ChartTypeGroup typeGroup;

	protected ChartOFCView preview;
	protected ContentPanel center;
	protected PivotGridPanel gridPanel;

	protected IndicatorTreePanel indicatorPanel;
	protected AdminFilterPanel adminFilterPanel;
	protected DateRangePanel dateFilterPanel;
	protected PartnerFilterPanel partnerFilterPanel;

	protected ComboBox<Dimension> legendCombo;

	protected LabelToolItem categoryLabel;
	protected LabelToolItem legendLabel;

	public AbstractChart(EventBus eventBus, Dispatcher service) {
		this.eventBus = eventBus;
		this.service = service;

		setLayout(new BorderLayout());

		createWest();
		createCenter();
		createToolBar();
		createChartPane();
		createDimBar();
		createGridPane();
	}

	protected void createWest() {

		ContentPanel westPanel = new ContentPanel(new AccordionLayout());
		westPanel.setHeading(I18N.CONSTANTS.filter());

		indicatorPanel = new IndicatorTreePanel(service, true);
		indicatorPanel.setHeaderVisible(true);
		westPanel.add(indicatorPanel);

		adminFilterPanel = new AdminFilterPanel(service);
		westPanel.add(adminFilterPanel);

		dateFilterPanel = new DateRangePanel();
		westPanel.add(dateFilterPanel);

		partnerFilterPanel = new PartnerFilterPanel(service);
		partnerFilterPanel.applyBaseFilter(new Filter());
		westPanel.add(partnerFilterPanel);

		BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST,
				0.30f);
		west.setCollapsible(true);
		west.setSplit(true);
		west.setMargins(new Margins(0, 5, 0, 0));

		add(westPanel, west);
	}

	protected void createCenter() {

		center = new ContentPanel(new BorderLayout());
		center.setHeaderVisible(false);

		add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));
	}

	protected void createToolBar() {
		toolBar = new ActionToolBar(this);

		toolBar.addRefreshButton();
		toolBar.add(new SeparatorToolItem());

		typeGroup = new ChartTypeGroup();
		typeGroup.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				updateLabels();
				load();
			}
		});

		toolBar.add(new LabelToolItem(I18N.CONSTANTS.chartType()));
		toolBar.add(typeGroup.getButtons());

		toolBar.add(new SeparatorToolItem());

		center.setTopComponent(toolBar);
	}

	protected void createChartPane() {

		preview = new ChartOFCView();
		center.add(preview, new BorderLayoutData(Style.LayoutRegion.CENTER));
	}

	protected void createGridPane() {
		BorderLayoutData south = new BorderLayoutData(Style.LayoutRegion.SOUTH,
				0.30f);
		south.setCollapsible(true);
		south.setSplit(true);
		south.setMargins(new Margins(5, 0, 0, 0));

		gridPanel = new PivotGridPanel(eventBus);
		gridPanel.setHeading("Table");

		center.add(gridPanel, south);
	}

	protected void createDimBar() {
		ToolBar dimBar = new ToolBar();
		ListStore<Dimension> store = DimensionStoreFactory.create(service);
			
		categoryLabel = new LabelToolItem();
		updateCategoryComboLabel(I18N.CONSTANTS.horizontalAxis());
		dimBar.add(categoryLabel);

		categoryCombo = new ComboBox<Dimension>();
		categoryCombo.setForceSelection(true);
		categoryCombo.setEditable(false);
		categoryCombo.setStore(store);
		categoryCombo.setDisplayField("caption");
		categoryCombo.setValue(new DateDimension(DateUnit.YEAR));
		dimBar.add(categoryCombo);

		dimBar.add(new FillToolItem());

		legendLabel = new LabelToolItem();
		updateLegendComboLabel(I18N.CONSTANTS.bars());
		dimBar.add(legendLabel);

		legendCombo = new ComboBox<Dimension>();
		legendCombo.setForceSelection(true);
		legendCombo.setEditable(false);
		legendCombo.setStore(store);
		legendCombo.setDisplayField("caption");
		dimBar.add(legendCombo);

		preview.setBottomComponent(dimBar);
	}

	protected void updateLabels() {
		Type type = typeGroup.getSelection();
		if (type == Type.ClusteredBar) {
			updateCategoryComboLabel(I18N.CONSTANTS.horizontalAxis());
			updateLegendComboLabel(I18N.CONSTANTS.bars());
		} else if (type == Type.Line) {
			updateCategoryComboLabel(I18N.CONSTANTS.horizontalAxis());
			updateLegendComboLabel(I18N.CONSTANTS.lines());
		} else if (type == Type.Pie) {
			updateCategoryComboLabel(I18N.CONSTANTS.slices());
			updateLegendComboLabel(I18N.CONSTANTS.disabled());
		}
	}

	protected void updateCategoryComboLabel(String label) {
		categoryLabel.setLabel(label);
	}

	protected void updateLegendComboLabel(String label) {
		legendLabel.setLabel(label);
	}

	public PivotChartReportElement getChartElement() {
		PivotChartReportElement element = new PivotChartReportElement();
		element.setType(typeGroup.getSelection());

		if (categoryCombo.getValue() instanceof DateDimension) {
			DateDimension dim = (DateDimension) categoryCombo.getValue();
			if (dim.getUnit() != DateUnit.YEAR) {
				element.addCategoryDimension(new DateDimension(DateUnit.YEAR));
			}
		}

		if (categoryCombo.getValue() != null) {
			element.addCategoryDimension(categoryCombo.getValue());
		}

		for (Integer indicatorId : indicatorPanel.getSelectedIds()) {
			element.addIndicator(indicatorId);
		}

		if (legendCombo.getValue() != null) {
			element.addSeriesDimension(legendCombo.getValue());
		}

		List<PartnerDTO> partners = partnerFilterPanel.getSelection();
		for (PartnerDTO entity : partners) {
			element.getFilter().addRestriction(DimensionType.Partner,
					entity.getId());
		}

		return element;
	}

	protected void load() {
		final PivotChartReportElement element = getChartElement();
		service.execute(new GenerateElement<PivotChartContent>(element), null,
				new AsyncCallback<PivotChartContent>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PivotChartContent result) {
						element.setContent(result);
						setData(element);
					}
				});
	}

	public void setData(PivotChartReportElement element) {
		preview.setContent(element);
		gridPanel.setData(element);
	}

	@Override
	public void onUIAction(String actionId) {
		if (UIActions.REFRESH.equals(actionId)) {
			load();
		}
	}

}
