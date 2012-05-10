package org.activityinfo.client.report.editor.map;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.common.toolbar.ActionListener;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.ExportMenuButton;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.page.report.editor.ReportElementEditor;
import org.activityinfo.client.report.editor.map.layerOptions.LayerOptionsPanel;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;

public class MapEditor extends ContentPanel implements ReportElementEditor<MapReportElement> {

	private static final int CONTROL_TOP_MARGIN = 10;
	private static final int LAYERS_STYLE_TOP_MARGIN = 50;
	private static final int ZOOM_CONTROL_LEFT_MARGIN = 10;

	private final Dispatcher dispatcher;
	private final ReportEventHelper events;
	private final EventBus eventBus;

	// Contained widgets
	private MapEditorMapView mapPanel;
	private LayersWidget layersWidget;
	private LayerOptionsPanel optionsPanel;
	private MapReportElement mapReportElement = new MapReportElement();
	private AbsoluteLayout layout;

	@Inject
	public MapEditor(Dispatcher dispatcher, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.events = new ReportEventHelper(eventBus, this);
		this.events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
			}
		});

		MapResources.INSTANCE.style().ensureInjected();

		initializeComponent();

		createMap();
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

		optionsPanel.addListener(Events.Hide, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				mapPanel.setZoomControlOffsetX(ZOOM_CONTROL_LEFT_MARGIN);
			}
		});

		optionsPanel.addListener(Events.Show, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				mapPanel.setZoomControlOffsetX(LayerOptionsPanel.WIDTH
						+ ZOOM_CONTROL_LEFT_MARGIN);
			}
		});
		
		optionsPanel.addValueChangeHandler(new ValueChangeHandler<MapLayer>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<MapLayer> event) {
				events.fireChange();
			}
		});

		add(optionsPanel, new AbsoluteData(0, CONTROL_TOP_MARGIN));
	}
	
	private void onChanged() {
		
	}

	protected void createLayersWidget() {
		layersWidget = new LayersWidget(dispatcher, eventBus, optionsPanel);

		// positioning is actually only set when setSize() is called below
		add(layersWidget, new AbsoluteData());
	}

	protected void createMap() {

		mapPanel = new MapEditorMapView(dispatcher, eventBus);
		mapPanel.setHeading(I18N.CONSTANTS.preview());
		mapPanel.setZoomControlOffsetX(ZOOM_CONTROL_LEFT_MARGIN);

		AbsoluteData layout = new AbsoluteData();
		layout.setLeft(0);
		layout.setTop(0);
		layout.setAnchorSpec("100% 100%");
		add(mapPanel, layout);
	}

	public AsyncMonitor getMapLoadingMonitor() {
		return new MaskingAsyncMonitor(mapPanel, I18N.CONSTANTS.loading());
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
	public void bind(MapReportElement model) {
		this.mapReportElement = model;
	
		layersWidget.bind(model);
		mapPanel.bind(model);
	}
	
	
	
	@Override
	public void disconnect() {
		events.disconnect();
		layersWidget.disconnect();
		mapPanel.disconnect();
	}

	@Override
	public MapReportElement getModel() {
		return mapReportElement;
	}

	@Override
	public Component getWidget() {
		return this;
	}

	@Override
	public List<Format> getExportFormats() {
		return Arrays.asList(Format.PowerPoint, Format.Word, Format.PDF, Format.PNG);
	}
}
