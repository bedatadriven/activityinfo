/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.map.layerOptions.LayerOptionsPanel;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;

/**
 * Displays a map where the user can interactively create a map
 */
public class MapPage extends ContentPanel implements Page, ExportCallback, ActionListener  {

	public static final PageId PAGE_ID = new PageId("maps");

    private static final int CONTROL_TOP_MARGIN = 10;
    private static final int LAYERS_STYLE_TOP_MARGIN = 50;
    private static final int ZOOM_CONTROL_LEFT_MARGIN = 10;
    private EventBus eventBus;
	
    // Data mechanics
    private final Dispatcher dispatcher;
    
    // Contained widgets
    private AIMapWidget aiMapWidget;
    private ActionToolBar toolbarMapActions;
    private LayersWidget layersWidget;
    private LayerOptionsPanel optionsPanel;
    private MapReportElement mapReportElement = new MapReportElement();
	private ExportMenuButton exportMenu;
	private AbsoluteLayout layout;

    @Inject
    public MapPage(Dispatcher dispatcher, EventBus eventBus) {
    	this.dispatcher = dispatcher;
    	this.eventBus=eventBus;
    	
    	MapResources.INSTANCE.style().ensureInjected();
    	
        initializeComponent();
       
        createMap();
        createToolBar();
        createLayersOptionsPanel();
        createLayersWidget();
    }

	private void initializeComponent() {
		setHeaderVisible(false);
		layout = new AbsoluteLayout();
		setLayout(layout);
	}

	private void createLayersOptionsPanel() {
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
				aiMapWidget.setZoomControlOffsetX(LayerOptionsPanel.WIDTH + ZOOM_CONTROL_LEFT_MARGIN);
			}
		});
		
		add(optionsPanel, new AbsoluteData(0, CONTROL_TOP_MARGIN));
	}
	
    private void createLayersWidget() {
        layersWidget = new LayersWidget(dispatcher, optionsPanel);
        layersWidget.setValue(mapReportElement);
        
        layersWidget.addValueChangeHandler(new ValueChangeHandler<MapReportElement>() {
			@Override
			public void onValueChange(ValueChangeEvent<MapReportElement> event) {
				aiMapWidget.setValue(event.getValue());
				layersWidget.setValue(event.getValue());
				boolean canExport = !event.getValue().getLayers().isEmpty();
				toolbarMapActions.setActionEnabled(UIActions.exportData,canExport); 
				exportMenu.setEnabled(canExport);
			}
		});

        // positioning is actually only set when setSize() is called below
        add(layersWidget, new AbsoluteData());
	}

    private void createMap() {
    		
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
        exportMenu = new ExportMenuButton(RenderElement.Format.PowerPoint)
        	.withPowerPoint()
        	.withWord()
        	.withPdf()
        	.withPng()
        	.callbackTo(this);
        exportMenu.setEnabled(false);
		toolbarMapActions.add(exportMenu);
        toolbarMapActions.addButton(UIActions.exportData, I18N.CONSTANTS.exportData(),
                IconImageBundle.ICONS.excel());
        toolbarMapActions.setActionEnabled(UIActions.exportData, false);
        
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
		layout.setPosition(layersWidget, width - LayersWidget.WIDTH, CONTROL_TOP_MARGIN);
		
		super.setSize(width, height);
	}

	@Override
	public void shutdown() {		
		layersWidget.shutdown();
	}

	@Override
	public void onUIAction(String actionId) {
		if (actionId.equals(UIActions.exportData)) {
			export(Format.Excel_Data);
		}
	}

	@Override
	public void export(Format format) {
		RenderElement renderElement = new RenderElement();
		renderElement.setElement(aiMapWidget.getValue());
		renderElement.setFormat(format);
		dispatcher.execute(renderElement, null, new DownloadCallback(eventBus));
	}

	@Override
	public PageId getPageId() {
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return true;
	}
}

