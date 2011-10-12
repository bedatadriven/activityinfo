package org.sigmah.client.page.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Image;


public class SearchFilterView extends ContentPanel {
	private Map<DimensionType, List<SearchResultEntity>> affectedEntities;
	private static List<DimensionType> supportedDimensions = new ArrayList<DimensionType>();
	private Map<DimensionType, EntityPanel> dimensionPanels = new HashMap<DimensionType, EntityPanel>();
	private EventBus eventBus = new SimpleEventBus();
	
	static {
		supportedDimensions.add(DimensionType.AdminLevel);
		supportedDimensions.add(DimensionType.Project);
		supportedDimensions.add(DimensionType.Partner);
		supportedDimensions.add(DimensionType.Location);
		supportedDimensions.add(DimensionType.AttributeGroup);
	}
	
	public SearchFilterView() {
		super();
		
		SearchResources.INSTANCE.searchStyles().ensureInjected();
		setStylePrimaryName("filterView");
		setHeading(I18N.CONSTANTS.showingSearchResultFor());
		setHeight(350);
		//setStyleAttribute("height", "auto"); 
		//setHeight("auto");
		
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		layout.setPadding(new Padding(4,4,4,4));
		setLayout(layout);

		for (DimensionType dimension : supportedDimensions) {
			EntityPanel entityPanel = new EntityPanel(dimension);
			dimensionPanels.put(dimension, entityPanel);
			add(entityPanel);
			entityPanel.addDimensionAddedHandler(new DimensionAddedEventHandler() {
				@Override
				public void onDimensionAdded(DimensionAddedEvent event) {
					eventBus.fireEvent(new DimensionAddedEvent(event.getAddedEntity()));
				}
			});
		}
		layout(true);
	}

	private void updateUI() {
		for (DimensionType foundEntity : supportedDimensions) {
			dimensionPanels.get(foundEntity).fillWithEntities(affectedEntities.get(foundEntity));
		}
		
		layout(true);
	}
	
	public void setFilter(Map<DimensionType, List<SearchResultEntity>> affectedEntities) {
		this.affectedEntities = affectedEntities;

		updateUI();		
	}
	
	public void addDimensionAddedHandler(DimensionAddedEventHandler handler) {
		eventBus.addHandler(DimensionAddedEvent.TYPE, handler);
	}
	
	public interface DimensionAddedEventHandler extends EventHandler {
		public void onDimensionAdded(DimensionAddedEvent event);
	}
	
	public static class DimensionAddedEvent extends GwtEvent<DimensionAddedEventHandler> {
		public final static Type<DimensionAddedEventHandler> TYPE = new Type<DimensionAddedEventHandler>();
		private SearchResultEntity addedEntity;
		
		public DimensionAddedEvent(SearchResultEntity addedEntity) {
			this.addedEntity = addedEntity;
		}

		public SearchResultEntity getAddedEntity() {
			return addedEntity;
		}

		@Override
		public Type<DimensionAddedEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(DimensionAddedEventHandler handler) {
			handler.onDimensionAdded(this);
		}

	}
	
	private class EntityPanel extends VerticalPanel {
		private DimensionType dimension;
		private HorizontalPanel panelHeader;
		private ListStore<BaseModelData> store = new ListStore<BaseModelData>();
		private ListView<BaseModelData> listviewEntities = new ListView<BaseModelData>(store);
		private LabelField labelNoSearch = new LabelField(I18N.CONSTANTS.noSearch());
		private LabelField labelNoMatches = new LabelField(I18N.CONSTANTS.noMatches());
		private EventBus eventBus = new SimpleEventBus();

		public EntityPanel(DimensionType dimension) {
			super();
			
			this.dimension = dimension;

			SearchResources.INSTANCE.searchStyles().ensureInjected();
			
			initializeComponent();
		}

		public void addDimensionAddedHandler(DimensionAddedEventHandler handler) {
			eventBus.addHandler(DimensionAddedEvent.TYPE, handler);
		}

		private void initializeComponent() {
			listviewEntities.setTemplate(SearchResources.INSTANCE.entitiesTemplate().getText());

			panelHeader = new HorizontalPanel();
			panelHeader.setStyleAttribute("margin", "4px");
			Image icon = IconImageBundle.fromEntities.fromDimension(dimension).createImage();
			icon.setStylePrimaryName(".entityIcon");
			panelHeader.add(icon);
			panelHeader.add(new LabelField(I18N.fromEntities.getDimensionTypePluralName(dimension)));
			add(panelHeader);
			
			labelNoMatches.setStyleAttribute("color", "grey");
			labelNoSearch.setStyleAttribute("color", "grey");
			add(labelNoSearch);
			
			addListenerToListview();
			listviewEntities.setItemSelector(".searchSmall");
			listviewEntities.setSelectStyle(".searchSmallSelect");
			listviewEntities.setStyleAttribute("margin", "4px 8px 4px 22px");
		}

		private void addListenerToListview() {
			listviewEntities.addListener(Events.Select, new Listener<ListViewEvent>() {
				@Override
				public void handleEvent(ListViewEvent be) {
					if (be.getTargetEl().hasStyleName("searchSmall")) { // icon: add to search box
						eventBus.fireEvent(new DimensionAddedEvent(
								(SearchResultEntity) be.getModel()));
					}
				}
			});
		}
		
		public void fillWithEntities(List<SearchResultEntity> affectedEntities) {
			removeAll();
			add(panelHeader);

			if (affectedEntities == null) {
				add(labelNoMatches);
			} else {
				store.removeAll();
				store.add(affectedEntities);
				add(listviewEntities);
			}
		}
		
	}
}
