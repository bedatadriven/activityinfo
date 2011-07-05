package org.sigmah.client.page.map;

import org.sigmah.shared.report.model.BubbleMapLayer;
import org.sigmah.shared.report.model.MapElement;
import org.sigmah.shared.report.model.MapLayer;

import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/*
 * Displays a list of layers selected by the user
 * 
 * @author Ruud Poutsma
 */
public class SelectedLayerList extends LayoutContainer implements HasValue<MapElement> {
	private MapElement mapElement;
	ListStore<LayerModel> store = new ListStore<LayerModel>();
	ListView<LayerModel> view = new ListView<LayerModel>();
	ContentPanel panel = new ContentPanel();
	LayerOptions layerOptions = new LayerOptions();
	
	public SelectedLayerList() {
		super();
		
		setLayout(new FitLayout());
		
		createPanel();
		createListView();
		createLayerOptions();
		
		panel.add(view, new FormData("100%"));
		panel.add(layerOptions);
		add(panel);
	}

	private void createLayerOptions() {
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
		
		view.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<LayerModel>>() {
					@Override
					public void handleEvent(SelectionChangedEvent<LayerModel> be) {
						
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
				
				// Remove layer
				if (be.getTargetEl().hasStyleName("removeLayer"))
				{
					store.remove((LayerModel) be.getModel());
					mapElement.getLayers().remove(be.getIndex());
					ValueChangeEvent.fire(SelectedLayerList.this, mapElement);
				}
				
				// Visibility
				if (be.getTargetEl().hasStyleName("checkbox"))
				{
					System.out.println(be.getTargetEl().getStyleName());
					LayerModel layerModel = (LayerModel) be.getModel();
					if (layerModel != null)
					{
						layerModel.setVisible(!layerModel.isVisible());
					}
				}
			}
		});
		
	   ListViewDropTarget target = new ListViewDropTarget(view);
	   target.setAllowSelfAsSource(true);
	   target.setFeedback(Feedback.INSERT);
	}
	
	private void removeLayer()
	{
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MapElement> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapElement getValue() {
		return mapElement;
	}

	@Override
	public void setValue(MapElement value) {
		setValue(value, false);
	}

	@Override
	public void setValue(MapElement value, boolean fireEvents) {
		this.mapElement=value;
		
		// Update store here
		store.removeAll();
		for (MapLayer layer : value.getLayers())
		{
			LayerModel model =  new LayerModel();
			if (layer instanceof BubbleMapLayer)
			{
				model.setName("layer X");
				store.add(model);
			}
		}
	}

}
