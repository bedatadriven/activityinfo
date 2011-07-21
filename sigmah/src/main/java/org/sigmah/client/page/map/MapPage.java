/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
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
import org.sigmah.client.page.map.mapOptions.AllMapOptionsWidget;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.model.MapReportElement;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;

/**
 * Displays a map where the user can interactively create a map
 */
public class MapPage extends ContentPanel implements Page, ExportCallback, ActionListener  {

    public static final PageId PAGE_ID = new PageId("maps");
	
    // Data mechanics
    private final Dispatcher dispatcher;
    
    // Contained widgets
    private final AllMapOptionsWidget allMapOptionsWidget;
    private AIMapWidget aiMapWidget;
    private ActionToolBar toolbarMapActions;
    private LayersWidget layersWidget;
    private MapReportElement mapReportElement = new MapReportElement();

    @Inject
    public MapPage(Dispatcher dispatcher, final AllMapOptionsWidget form) {
    	this.dispatcher = dispatcher;
    	
    	MapResources.INSTANCE.layerStyle().ensureInjected();
    	
        initializeComponent();
        
        this.allMapOptionsWidget = form;
        
        createFormPane(form);
        createMap();
        createToolBar();
        createSelectedLayersWidget();
    }

	private void initializeComponent() {
		setLayout(new BorderLayout());
        setHeaderVisible(false);
	}

    private void createSelectedLayersWidget() {
        layersWidget = new LayersWidget(dispatcher);
        
        BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST, 0.25f);
        east.setCollapsible(true);
        east.setSplit(true);
        east.setMargins(new Margins(0, 0, 0, 5));
        
        layersWidget.addValueChangeHandler(new ValueChangeHandler<MapReportElement>() {
			@Override
			public void onValueChange(ValueChangeEvent<MapReportElement> event) {
				aiMapWidget.setValue(event.getValue());
				layersWidget.setValue(event.getValue());
			}
		});
        
        add(layersWidget, east);
	}

	private void createFormPane(AllMapOptionsWidget allMapOptionsWidget) {
        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.20f);
        west.setCollapsible(true);
        
        west.setSplit(true);
        west.setMargins(new Margins(0, 5, 0, 0));

        add((Component) allMapOptionsWidget, west);
        
        allMapOptionsWidget.getMapOptionsWidget().addValueChangeHandler(new ValueChangeHandler<BaseMap>() {
			@Override
			public void onValueChange(ValueChangeEvent<BaseMap> event) {
				MapReportElement map = aiMapWidget.getValue();
				map.setBaseMapId(event.getValue().getId());
				aiMapWidget.setValue(map);
			}
		});
        
    }

    private void createMap() {
        aiMapWidget = new AIMapWidget(dispatcher);
        aiMapWidget.setHeading(I18N.CONSTANTS.preview());

        add(aiMapWidget, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    protected void createToolBar() {
        toolbarMapActions = new ActionToolBar(this);
        toolbarMapActions.addRefreshButton();
        toolbarMapActions.add(new ExportMenuButton(RenderElement.Format.PowerPoint, new ExportCallback() {
            public void export(RenderElement.Format format) {
               export(format);

            }
        }));
        toolbarMapActions.addButton(UIActions.exportData, I18N.CONSTANTS.exportData(),
                IconImageBundle.ICONS.excel());
        aiMapWidget.setTopComponent(toolbarMapActions);
    }

    public AsyncMonitor getMapLoadingMonitor() {
        return new MaskingAsyncMonitor(aiMapWidget, I18N.CONSTANTS.loading());
    }

	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
	}

	@Override
	public void shutdown() {		
	}

	@Override
	public void onUIAction(String actionId) {
		
	}

	@Override
	public void export(Format format) {
		// TODO Auto-generated method stub
	}
//	    public ReportElement getMapElement() {
//      return form.getMapElement();
//  }

//  public boolean validate() {
//      return form.validate();
//  }

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

