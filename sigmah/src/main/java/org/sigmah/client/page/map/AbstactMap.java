package org.sigmah.client.page.map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.map.layerOptions.LayerOptionsPanel;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.layers.MapLayer;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;

public abstract class AbstactMap extends ContentPanel implements ActionListener {

	protected static final int CONTROL_TOP_MARGIN = 10;
	protected static final int LAYERS_STYLE_TOP_MARGIN = 50;
	protected static final int ZOOM_CONTROL_LEFT_MARGIN = 10;
	protected EventBus eventBus;

	// Data mechanics
	protected final Dispatcher dispatcher;

	// Contained widgets
	protected AIMapWidget aiMapWidget;
	protected ActionToolBar toolbarMapActions;
	protected LayersWidget layersWidget;
	protected LayerOptionsPanel optionsPanel;
	protected MapReportElement mapReportElement = new MapReportElement();
	protected ExportMenuButton exportMenu;
	protected AbsoluteLayout layout;

	@Inject
	public AbstactMap(Dispatcher dispatcher, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;

		MapResources.INSTANCE.style().ensureInjected();

		initializeComponent();

		createMap();
		createToolBar();
		createLayersOptionsPanel();
		createLayersWidget();
	}

	protected void initializeComponent() {
		setHeaderVisible(false);
		layout = new AbsoluteLayout();
		setLayout(layout);
	}

	protected void createLayersOptionsPanel() {
		optionsPanel = new LayerOptionsPanel(dispatcher);
		optionsPanel.setVisible(false);
		optionsPanel.addValueChangeHandler(new ValueChangeHandler<MapLayer>() {

			@Override
			public void onValueChange(ValueChangeEvent<MapLayer> event) {
				int l = mapReportElement.getLayers().indexOf(event.getValue());
				aiMapWidget.setValue(mapReportElement);
				layersWidget.setValue(mapReportElement);
			}
		});

		optionsPanel.addListener(Events.Hide, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				aiMapWidget.setZoomControlOffsetX(ZOOM_CONTROL_LEFT_MARGIN);
			}
		});

		optionsPanel.addListener(Events.Show, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				aiMapWidget.setZoomControlOffsetX(LayerOptionsPanel.WIDTH
						+ ZOOM_CONTROL_LEFT_MARGIN);
			}
		});

		add(optionsPanel, new AbsoluteData(0, CONTROL_TOP_MARGIN));
	}

	protected void createLayersWidget() {
		layersWidget = new LayersWidget(dispatcher, optionsPanel);
		layersWidget.setValue(mapReportElement);

		layersWidget
				.addValueChangeHandler(new ValueChangeHandler<MapReportElement>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<MapReportElement> event) {
						aiMapWidget.setValue(event.getValue());
						layersWidget.setValue(event.getValue());
						boolean canExport = !event.getValue().getLayers()
								.isEmpty();
						toolbarMapActions.setActionEnabled(
								UIActions.EXPORT_DATA, canExport);
						exportMenu.setEnabled(canExport);
					}
				});

		// positioning is actually only set when setSize() is called below
		add(layersWidget, new AbsoluteData());
	}

	protected void createMap() {

		aiMapWidget = new AIMapWidget(dispatcher);
		aiMapWidget.setHeading(I18N.CONSTANTS.preview());
		aiMapWidget.setZoomControlOffsetX(ZOOM_CONTROL_LEFT_MARGIN);

		// Ugly hack to prevent call avalanches
		aiMapWidget.setMaster(this);

		AbsoluteData layout = new AbsoluteData();
		layout.setLeft(0);
		layout.setTop(0);
		layout.setAnchorSpec("100% 100%");
		add(aiMapWidget, layout);
	}

	protected void createToolBar() {
		toolbarMapActions = new ActionToolBar(this);
	
		setTopComponent(toolbarMapActions);
	}

	public AsyncMonitor getMapLoadingMonitor() {
		return new MaskingAsyncMonitor(aiMapWidget, I18N.CONSTANTS.loading());
	}

	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
	}

	@Override
	public void setSize(int width, int height) {
		// right align side bar
		layout.setPosition(layersWidget, width - LayersWidget.WIDTH,
				CONTROL_TOP_MARGIN);

		super.setSize(width, height);
	}

	@Override
	public void onUIAction(String actionId) {

	}

}
