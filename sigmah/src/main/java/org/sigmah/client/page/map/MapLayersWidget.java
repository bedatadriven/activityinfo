package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.report.model.layers.BubbleMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.SourceSelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Layer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;

/*
 * Displays a list of layers selected by the user 
 */
public class MapLayersWidget extends LayoutContainer implements HasValue<MapReportElement> {
	private Dispatcher service;
	private MapReportElement mapElement;
	private ListStore<MapLayerModel> store = new ListStore<MapLayerModel>();
	private ListView<MapLayerModel> view = new ListView<MapLayerModel>();
	private ContentPanel panel = new ContentPanel();
	private LayerOptions layerOptions = new LayerOptions();
	private Button buttonAddLayer = new Button();
	private AddLayerDialog addLayer;
	
	@Inject
	public MapLayersWidget(Dispatcher service) {
		super();
		
		this.service = service;
		
		createDefaultMapReportElement();
		
		setLayout(new FlowLayout());
		
		createPanel();
		createAddLayerButton();
		createListView();
		createAddLayersDialog();
		
		panel.add(view, new FormData("100%"));
		panel.addButton(buttonAddLayer);
		panel.add(layerOptions);
		add(panel);
	}

	private void createDefaultMapReportElement() {
		 mapElement = new MapReportElement();
	}

	private void createAddLayersDialog() {
		addLayer = new AddLayerDialog(service);
		addLayer.addValueChangeHandler(new ValueChangeHandler<MapLayer>(){
			@Override
			public void onValueChange(ValueChangeEvent<MapLayer> event) {
				if (event.getValue() != null) {
					addLayer(event.getValue());
				}
			}});
	}

	private void createAddLayerButton() {
		buttonAddLayer.setText("Add layer...");
		buttonAddLayer.addListener(Events.Select, new SelectionListener<ButtonEvent>() {  
		      @Override  
		      public void componentSelected(ButtonEvent ce) {  
		    	  addLayer.show();
		      }
		});  
	}

	private void createPanel() {
		panel.setCollapsible(false);
		panel.setFrame(true);
		panel.setHeading("Selected Layers");
		panel.setAutoHeight(true);
		panel.setAutoWidth(true);
		panel.setBodyBorder(false);
	}					


	private void createListView() {
		view.setStore(store);
		view.setTemplate(MapResources.INSTANCE.layerTemplate().getText());
		view.setItemSelector(".layerItem");
		
		// Prevents confusion for the user where an onmouseover-ed item in the listview *looks* selected,
		// but in fact is not selected
		
		// This option does not actually fire a select event :(
		view.setSelectOnOver(true);
		
		view.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<MapLayerModel>>() {
					@Override
					public void handleEvent(SelectionChangedEvent<MapLayerModel> be) {
						
					}
				});

		ListViewDragSource source = new ListViewDragSource(view) {
			@Override
			protected void onDragStart(DNDEvent e) {
				super.onDragStart(e);
				if (!e.getTargetEl().hasStyleName("grabSprite")) {
					e.setCancelled(true);
				}
			}
			@Override
			protected void onDragDrop(DNDEvent e) {
				super.onDragDrop(e);
			}
		};
		
		view.addListener(Events.Select, new Listener<ListViewEvent>() {
			@Override
			public void handleEvent(ListViewEvent be) {
				// Change visibility
				if (be.getTargetEl().hasStyleName("x-view-item-checkbox"))
				{
					MapLayerModel layerModel = (MapLayerModel) be.getModel();
					if (layerModel != null)
					{
						layerModel.setVisible(!layerModel.isVisible());
					}
				}				
				
				// Remove 
				if (be.getTargetEl().hasStyleName("x-view-item-button"))
				{
					if (view.getSelectionModel().getSelectedItem() != null)
					{
						int index = store.indexOf(view.getSelectionModel().getSelectedItem());
						removeLayer(mapElement.getLayers().get(index));
					}
				} 
			}
		});			
		
		view.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<MapLayerModel>() {
			@Override()
			public void selectionChanged(SelectionChangedEvent<MapLayerModel> se) {
				if (se.getSelectedItem()==null) {
					System.out.println("Selected item on maplayersview is null :(");
				} else {
					layerOptions.setMapLayer(se.getSelectedItem().getMapLayer());
				}
			}
		});
		
		
	    ListViewDropTarget target = new ListViewDropTarget(view);
	    target.setAllowSelfAsSource(true);
	    target.setFeedback(Feedback.INSERT);
	}
	
	private void removeLayer(MapLayer mapLayer) {
		mapElement.getLayers().remove(mapLayer);				
		ValueChangeEvent.fire(this, mapElement);
		updateStore();
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
		updateStore();
	}

	private void updateStore() {
		store.removeAll();
		if (mapElement != null)
		{
			for (MapLayer layer : mapElement.getLayers())
			{
				MapLayerModel model =  new MapLayerModel();
				model.setName("layer X");
				model.setVisible(layer.isVisible());
				model.setMapLayer(layer);
				store.add(model);
			}
		}
	}

	public void addLayer(MapLayer layer) {
		layer.setClustering(new NoClustering());
		mapElement.getLayers().add(layer);
		ValueChangeEvent.fire(this, mapElement);
		updateStore();
	}
}
