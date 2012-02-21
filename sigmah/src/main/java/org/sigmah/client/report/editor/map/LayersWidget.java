package org.sigmah.client.report.editor.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.report.editor.map.layerOptions.LayerOptionsPanel;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;

/**
 * Displays a list of layers selected by the user 
 */
public final class LayersWidget extends LayoutContainer implements HasValue<MapReportElement> {

	public static final int WIDTH = 225;

	private static final int CONTEXT_MENU_WIDTH = 150;

	
	private Dispatcher service;
	private MapReportElement mapElement;
	private ListStore<LayerModel> store = new ListStore<LayerModel>();
	private ListView<LayerModel> view = new ListView<LayerModel>();

	private ContentPanel layersPanel;
	private AddLayerDialog addLayersDialog;
	private LayerOptionsPanel optionsPanel;
	private BaseMapPanel baseMapPanel;
	
	private Menu layerMenu;
	
	@Inject
	public LayersWidget(Dispatcher service, LayerOptionsPanel optionsPanel) {
		super();
		
		this.service = service;
		this.optionsPanel = optionsPanel;
		
		createDefaultMapReportElement();
		
		initializeComponent();
		createLayersPanel();

		createAddLayerButton();
		createListView();
		
		createBaseMapPanel();
	}

	private void createDefaultMapReportElement() {
		 mapElement = new MapReportElement();
	}

	private void createAddLayersDialog() {
		addLayersDialog = new AddLayerDialog(service);
		addLayersDialog.addValueChangeHandler(new ValueChangeHandler<MapLayer>(){
			@Override
			public void onValueChange(ValueChangeEvent<MapLayer> event) {
				if (event.getValue() != null) {
					addLayer(event.getValue());
				}
			}
		});
	}

	private void createAddLayerButton() {
		Button addLayerButton = new Button();
		addLayerButton.setText(I18N.CONSTANTS.add());
		addLayerButton.addListener(Events.Select, new SelectionListener<ButtonEvent>() {  
		      @Override  
		      public void componentSelected(ButtonEvent ce) {  
		    	  if(addLayersDialog == null) {
		    		  createAddLayersDialog();
		    	  }
		    	  addLayersDialog.show();
		      }
		});
		
		addLayerButton.setIcon(IconImageBundle.ICONS.add());
		
		layersPanel.getHeader().addTool(addLayerButton);
	}

	private void initializeComponent() {
		AnchorLayout anchorLayout = new AnchorLayout();
		setLayout(anchorLayout);
		
		setWidth(WIDTH);
	}					
	
	private void createLayersPanel() {
		layersPanel = new ContentPanel();
		layersPanel.setCollapsible(false);
		layersPanel.setFrame(true);
		layersPanel.setHeading(I18N.CONSTANTS.layers());
		layersPanel.setBodyBorder(false);
		layersPanel.setHeaderVisible(true);
		layersPanel.setIcon(AbstractImagePrototype.create(MapResources.INSTANCE.layers()));		
		
		AnchorData layoutData = new AnchorData();
		layoutData.setAnchorSpec("100% none");
		
		add(layersPanel, layoutData);
	}

	private void createBaseMapPanel() {
		baseMapPanel = new BaseMapPanel(service);
		baseMapPanel.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				mapElement.setBaseMapId(event.getValue());
				ValueChangeEvent.fire(LayersWidget.this, mapElement);
			}
		});
	
		AnchorData layoutData = new AnchorData();
		layoutData.setAnchorSpec("100% none");
	
		add(baseMapPanel, layoutData);
	}
	
	private void createListView() {
		view.setStore(store);
		view.setTemplate(MapResources.INSTANCE.layerTemplate().getText());
		view.setItemSelector(".layerItem");
		
		// Prevents confusion for the user where an onmouseover-ed item in the listview *looks* selected,
		// but in fact is not selected
		// Off for now. It's a choice between two evils: confusing layer removal and 
		//    confusing layer selection
		//view.setSelectOnOver(true);

		addListViewDnd();			
		
		view.addListener(Events.Select, new Listener<ListViewEvent<LayerModel>>() {
			@Override
			public void handleEvent(ListViewEvent<LayerModel> event) {
				onLayerSelected(event);
			}
		});
	    layersPanel.add(view);
	}


	private void onLayerSelected(ListViewEvent<LayerModel> event) {
		if (event.getIndex() == -1) {
			optionsPanel.hide();
		}
		// Change visibility
		if (event.getTargetEl().hasStyleName("x-view-item-checkbox")) {
			LayerModel layerModel = event.getModel();
			if (layerModel != null) {
				boolean newSetting = !layerModel.isVisible();
				layerModel.setVisible(newSetting);
				layerModel.getMapLayer().setVisible(newSetting);
				ValueChangeEvent.fire(LayersWidget.this, mapElement);
				store.update(layerModel);
			}
		} else {
			showOptionsMenu(event.getIndex());
		}
		optionsPanel.onLayerSelectionChanged(event.getModel().getMapLayer());
	}
	
	public void shutdown() {
		view.removeAllListeners();

		if(layerMenu != null) {
			layerMenu.hide();		
			layerMenu.removeAllListeners();
		}
		if(addLayersDialog != null) {
			addLayersDialog.hide();
			addLayersDialog.removeAllListeners();
		}
	}
	
	private MapLayer getSelectedLayer() {
		return view.getSelectionModel().getSelectedItem().getMapLayer();
	}

	private void showOptionsMenu(int index) {
		if(layerMenu == null) {
			createLayerMenu();
		}
		int x = this.getAbsoluteLeft() - CONTEXT_MENU_WIDTH;
		int y = view.getElement(index).getAbsoluteTop();
		
		layerMenu.showAt(x, y);
	}

	private void createLayerMenu() {
		layerMenu = new Menu();
		layerMenu.add(new MenuItem(I18N.CONSTANTS.style(), 
					AbstractImagePrototype.create(MapResources.INSTANCE.styleIcon()),
					new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				optionsPanel.showStyle(getSelectedLayer());
			}
		}));
		layerMenu.add(new MenuItem(I18N.CONSTANTS.clustering(),
					AbstractImagePrototype.create(MapResources.INSTANCE.clusterIcon()),
					new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				optionsPanel.showAggregation(getSelectedLayer());
			}
		}));
		layerMenu.add(new MenuItem(I18N.CONSTANTS.filter(),
				IconImageBundle.ICONS.filter(),
				new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				optionsPanel.showFilter(getSelectedLayer());
			}
		}));
	
		
		layerMenu.add(new SeparatorMenuItem());
		
		
		layerMenu.add(new MenuItem(I18N.CONSTANTS.delete(), 
					IconImageBundle.ICONS.delete(), 
					new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				removeLayer(getSelectedLayer());
			}
		}));
		layerMenu.setWidth(CONTEXT_MENU_WIDTH);
	}
	
	private void addListViewDnd() {
	    ListViewDropTarget target = new MapLayersDropTarget(view);
	    target.setAllowSelfAsSource(true);
	    target.setFeedback(Feedback.INSERT);

	    new LayerListViewDragSource(view);
	
	}
	
	private void removeLayer(MapLayer mapLayer) {
		mapElement.getLayers().remove(mapLayer);				
		ValueChangeEvent.fire(this, mapElement);
		updateStore();
		
		if(optionsPanel.getValue() == mapLayer) {
			optionsPanel.fadeOut();
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MapReportElement> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public MapReportElement getValue() {
		return mapElement;
	}

	@Override
	public void setValue(MapReportElement value) {
		setValue(value, false);
	}

	@Override
	public void setValue(MapReportElement value, boolean fireEvents) {
		this.mapElement=value;
		this.baseMapPanel.setValue(value.getBaseMapId());
		updateStore();
	}

	private void updateStore() {
		// Save the selecteditem, because removing all items from the store triggers
		// a selecteditem change
		int selectedItemIndex = store.indexOf(view.getSelectionModel().getSelectedItem());
		store.removeAll();
		if (mapElement != null) {
			for (MapLayer layer : mapElement.getLayers()) {
				LayerModel model =  new LayerModel();
				model.setName(layer.getName());
				model.setVisible(layer.isVisible());
				model.setMapLayer(layer);
				model.setLayerType(layer.getTypeName());
				store.add(model);
			}
		}
		
		// Place selection back at original selection
		if ((selectedItemIndex != -1) && (selectedItemIndex < store.getCount())) {
			List<LayerModel> selectedItem = new ArrayList<LayerModel>();
			selectedItem.add(store.getAt(selectedItemIndex));
			view.getSelectionModel().setSelection(selectedItem);
		}
	}

	public void addLayer(MapLayer layer) {
		layer.setClustering(new NoClustering());
		mapElement.getLayers().add(layer);
		ValueChangeEvent.fire(this, mapElement);
		updateStore();
	}
	

	private final class LayerListViewDragSource extends ListViewDragSource {
		private int draggedItemIndexStart = 0;
		private int draggedItemIndexDrop = 0;

		private LayerListViewDragSource(ListView listView) {
			super(listView);
		}

		@Override
		protected void onDragStart(DNDEvent e) {
			super.onDragStart(e);
			if (!e.getTargetEl().hasStyleName("grabSprite")) {
				e.setCancelled(true);
			}
			draggedItemIndexStart = store.indexOf(view.getSelectionModel().getSelectedItem());
			e.setData(draggedItemIndexStart);
		}

		@Override
		protected void onDragDrop(DNDEvent e) {
			super.onDragDrop(e);
			
			// Move the MapLayer onto his new position
			draggedItemIndexDrop = ((MapLayersDropTarget)e.getDropTarget()).getInsertIndex();
			if (draggedItemIndexDrop == mapElement.getLayers().size()){
				draggedItemIndexDrop--;
			} 
			Collections.swap(mapElement.getLayers(), draggedItemIndexStart, draggedItemIndexDrop);
			ValueChangeEvent.fire(LayersWidget.this, mapElement);
		}
	}

	private class MapLayersDropTarget extends ListViewDropTarget {
		public MapLayersDropTarget(ListView listView) {
			super(listView);
		}
    	public int getInsertIndex() {
    		return insertIndex;
    	}
	}


}
