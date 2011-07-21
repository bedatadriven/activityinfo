package org.sigmah.client.page.map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.layerOptions.AllLayerOptions;
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
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.inject.Inject;

/*
 * Displays a list of layers selected by the user 
 */
public class LayersWidget extends ContentPanel implements HasValue<MapReportElement> {
	private Dispatcher service;
	private MapReportElement mapElement;
	private ListStore<LayerModel> store = new ListStore<LayerModel>();
	private ListView<LayerModel> view = new ListView<LayerModel>();
	private AllLayerOptions layerOptions;
	private Button buttonAddLayer = new Button();
	private AddLayerDialog addLayer;
	
	@Inject
	public LayersWidget(Dispatcher service) {
		super();
		
		this.service = service;
		
		createDefaultMapReportElement();
		
		initializeComponent();
		createAddLayersDialog();

		createAddLayerButton();
		createListView();
		createLayerOptions();
	}

	private void createLayerOptions() {
		layerOptions = new AllLayerOptions(service);
		layerOptions.addValueChangeHandler(new ValueChangeHandler<MapLayer>() {
			@Override
			public void onValueChange(ValueChangeEvent<MapLayer> event) {
				updateStore();
				ValueChangeEvent.fire(LayersWidget.this, mapElement);
			}
		});

	    add(layerOptions);
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
			}
		});
	}

	private void createAddLayerButton() {
		buttonAddLayer.setText(I18N.CONSTANTS.addLayerWithDialogHint());
		buttonAddLayer.addListener(Events.Select, new SelectionListener<ButtonEvent>() {  
		      @Override  
		      public void componentSelected(ButtonEvent ce) {  
		    	  addLayer.show();
		      }
		});
		
		buttonAddLayer.setIcon(AbstractImagePrototype.create(MapResources.INSTANCE.addLayer()));
		
	    add(buttonAddLayer);
	}

	private void initializeComponent() {
		VBoxLayout vboxLayout = new VBoxLayout();
		vboxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		vboxLayout.setPadding(new Padding(5));
		
		setLayout(vboxLayout);
		setCollapsible(false);
		setFrame(true);
		setHeading(I18N.CONSTANTS.layers());
		setBodyBorder(false);
		setHeaderVisible(false);
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
		
		view.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LayerModel>() {
			@Override()
			public void selectionChanged(SelectionChangedEvent<LayerModel> se) {
				changeSelectedLayer();
			}
		});
		
		view.addListener(Events.Select, new Listener<ListViewEvent>() {
			@Override
			public void handleEvent(ListViewEvent be) {
				if (be.getIndex() == -1) {
					disableLayerOptions();
				} else {
					enableLayerOptions();
				}
				
				// Change visibility
				if (be.getTargetEl().hasStyleName("x-view-item-checkbox")) {
					LayerModel layerModel = (LayerModel) be.getModel();
					if (layerModel != null) {
						boolean newSetting = !layerModel.isVisible();
						layerModel.setVisible(newSetting);
						layerModel.getMapLayer().setVisible(newSetting);
						ValueChangeEvent.fire(LayersWidget.this, mapElement);
						store.update(layerModel);
					}
//					be.setCancelled(true);
//					be.stopEvent();
				}				
				
				// Remove 
				//if (be.getTargetEl().hasStyleName("x-view-item-button")) {
				if (be.getTargetEl().hasStyleName("removeLayer")) {
					if (view.getSelectionModel().getSelectedItem() != null) {
						int index = store.indexOf(view.getSelectionModel().getSelectedItem());
						removeLayer(mapElement.getLayers().get(index));
					}
				} 
				
				changeSelectedLayer();
			}
		});

	    VBoxLayoutData vbld = new VBoxLayoutData();
	    vbld.setFlex(1);
	    add(view, vbld);
	}

	private void enableLayerOptions() {
		layerOptions.setEnabled(true);
	}

	private void disableLayerOptions() {
		layerOptions.setEnabled(false);
	}
	
	private void changeSelectedLayer() {
		if (view.getSelectionModel().getSelectedItem() != null) {
			layerOptions.setMapLayer(view.getSelectionModel().getSelectedItem().getMapLayer());
			layout(true);
		}
	}

	private void addListViewDnd() {
	    ListViewDropTarget target = new ListViewDropTarget(view);
	    target.setAllowSelfAsSource(true);
	    target.setFeedback(Feedback.INSERT);

	    ListViewDragSource source = new ListViewDragSource(view) {
	    	int draggedItemIndexStart = 0;
	    	int draggedItemIndexDrop = 0;
	    	
			@Override
			protected void onDragStart(DNDEvent e) {
				super.onDragStart(e);
				if (!e.getTargetEl().hasStyleName("grabSprite")) {
					e.setCancelled(true);
				}
				draggedItemIndexStart = store.indexOf(view.getSelectionModel().getSelectedItem());
			}
			
			@Override
			protected void onDragDrop(DNDEvent e) {
				super.onDragDrop(e);
				
				// Move the MapLayer onto his new position
				draggedItemIndexDrop = store.indexOf(view.getSelectionModel().getSelectedItem());
				MapLayer removed = mapElement.getLayers().remove(draggedItemIndexStart);
				mapElement.getLayers().add(draggedItemIndexDrop, removed);
				
				ValueChangeEvent.fire(LayersWidget.this, mapElement);
			}
		};
	
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
	}

	public void addLayer(MapLayer layer) {
		layer.setClustering(new NoClustering());
		mapElement.getLayers().add(layer);
		ValueChangeEvent.fire(this, mapElement);
		updateStore();
	}
}
