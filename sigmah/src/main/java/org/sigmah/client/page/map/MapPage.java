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
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.model.MapElement;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.report.model.layers.AbstractMapLayer;
import org.sigmah.shared.report.model.layers.AutoMapLayer;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
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
    private final MapForm form;
    private MapElementView mapView;
    private ActionToolBar toolBar;
    private MapLayersView layerControl;
    private MapElement mapElement = new MapElement();

    @Inject
    public MapPage(Dispatcher dispatcher, final MapForm form) {
    	this.dispatcher = dispatcher;
    	
    	MapResources.INSTANCE.layerStyle().ensureInjected();
    	
        setLayout(new BorderLayout());
        setHeaderVisible(false);
        
        this.form = form;
        
        createFormPane(form);
        createMap();
        createToolBar();
        createSelectedLayersWidget();
        
        form.getIndicatorTree().addCheckChangedListener(new Listener<TreePanelEvent>(){
			@Override
			public void handleEvent(TreePanelEvent be) {
					MapLayer layer = new BubbleMapLayer();
					layer.setIndicatorIds(form.getIndicatorTree().getSelectedIds());
					layer.setClustered(false);
					mapElement.addLayer(layer);
					layerControl.setValue(mapElement);
					mapView.setValue(mapElement);
			}
        });        
    }

    private void createSelectedLayersWidget() {
        layerControl = new MapLayersView(dispatcher);
        
        BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST, 0.20f);
        east.setCollapsible(true);
        east.setSplit(true);
        east.setMargins(new Margins(0, 0, 0, 5));
        
        layerControl.addValueChangeHandler(new ValueChangeHandler<MapElement>() {
			@Override
			public void onValueChange(ValueChangeEvent<MapElement> event) {
				mapView.setValue(event.getValue());
			}
		});
        
        add(layerControl, east);
	}

	private void createFormPane(MapForm form) {
        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.20f);
        west.setCollapsible(true);
        
        west.setSplit(true);
        west.setMargins(new Margins(0, 5, 0, 0));

        add((Component) form, west);
    }

    private void createMap() {
        mapView = new MapElementView(dispatcher);
        mapView.setHeading(I18N.CONSTANTS.preview());

        add(mapView, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    protected void createToolBar() {
        toolBar = new ActionToolBar(this);
        toolBar.addRefreshButton();
        toolBar.add(new ExportMenuButton(RenderElement.Format.PowerPoint, new ExportCallback() {
            public void export(RenderElement.Format format) {
               //export(format);

            }
        }));
        toolBar.addButton(UIActions.exportData, I18N.CONSTANTS.exportData(),
                IconImageBundle.ICONS.excel());
        mapView.setTopComponent(toolBar);
    }

    public AsyncMonitor getMapLoadingMonitor() {
        return new MaskingAsyncMonitor(mapView, I18N.CONSTANTS.loading());
    }

    public ReportElement getMapElement() {
        return form.getMapElement();
    }

    public boolean validate() {
        return form.validate();
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

