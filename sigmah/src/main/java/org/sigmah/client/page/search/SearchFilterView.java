package org.sigmah.client.page.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;


public class SearchFilterView extends ContentPanel {
	private Map<DimensionType, List<SearchResultEntity>> affectedEntities;
	private static List<DimensionType> supportedDimensions = new ArrayList<DimensionType>();
	private Map<DimensionType, EntityPanel> dimensionPanels = new HashMap<DimensionType, EntityPanel>();
	
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
		setHeight(300);
		
		VBoxLayout layout = new VBoxLayout();
		layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
		layout.setPadding(new Padding(2,2,2,2));
		setLayout(layout);

		for (DimensionType dimension : supportedDimensions) {
			EntityPanel entityPanel = new EntityPanel(dimension);
			dimensionPanels.put(dimension, entityPanel);
			add(entityPanel);
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
	
	private class EntityPanel extends VerticalPanel {
		private DimensionType dimension;
		private HorizontalPanel panelHeader;
		private ListStore<BaseModelData> store = new ListStore<BaseModelData>();
		private ListView<BaseModelData> listviewEntities = new ListView<BaseModelData>(store);
		private LabelField labelNoSearch = new LabelField(I18N.CONSTANTS.noSearch());
		private LabelField labelNoMatches = new LabelField(I18N.CONSTANTS.noMatches());

		public EntityPanel(DimensionType dimension) {
			super();
			
			this.dimension = dimension;

			SearchResources.INSTANCE.searchStyles().ensureInjected();
			
			initializeComponent();
		}

		private void initializeComponent() {
			listviewEntities.setTemplate(SearchResources.INSTANCE.entitiesTemplate().getText());

			panelHeader = new HorizontalPanel();
			panelHeader.add(IconImageBundle.fromEntities.fromDimension(dimension).createImage());
			panelHeader.add(new LabelField(I18N.fromEntities.getDimensionTypePluralName(dimension)));
			add(panelHeader);
			
			labelNoMatches.setStyleAttribute("color", "grey");
			labelNoSearch.setStyleAttribute("color", "grey");
			add(labelNoSearch);
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
