package org.sigmah.client.page.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;


public class SearchFilterView extends ContentPanel {
	private String searchQuery = "";
	private Map<DimensionType, List<SearchResultEntity>> affectedEntities;
	private static List<DimensionType> supportedDimensions = new ArrayList<DimensionType>();
	private Map<DimensionType, ContentPanel> dimensionPanels = new HashMap<DimensionType, ContentPanel>();
	
	static {
		supportedDimensions.add(DimensionType.AdminLevel);
		supportedDimensions.add(DimensionType.Project);
		supportedDimensions.add(DimensionType.Partner);
		//supportedDimensions.add(DimensionType.Database);
		supportedDimensions.add(DimensionType.Location);
		supportedDimensions.add(DimensionType.AttributeGroup);
		//supportedDimensions.add(DimensionType.Indicator);
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

		VBoxLayoutData hbld = new VBoxLayoutData();
		hbld.setFlex(1);
//		hbld.setMinHeight(75);
//		hbld.setMaxHeight(150);

		for (DimensionType dimension : supportedDimensions) {
			ContentPanel panel = new ContentPanel();
			panel.setHeading(I18N.fromEntities.getDimensionTypePluralName(dimension));
			panel.setIcon(IconImageBundle.fromEntities.fromDimension(dimension));
			panel.setLayout(new RowLayout(Orientation.HORIZONTAL));
			dimensionPanels.put(dimension, panel);
			panel.add(new LabelField("No search"));
			add(panel, hbld);
		}
		layout(true);
	}


	public void setQuery(String query) {
		this.searchQuery=query;
	}

	private void updateUI() {
		setHasFilter(affectedEntities.size() > 0);
		showEntityPanel();
	}
	
	private void setHasFilter(boolean hasFilter) {
		// show the filters
	}


	private void showEntityPanel() {
		for (DimensionType foundEntity : supportedDimensions) {
			if (affectedEntities.containsKey(foundEntity)) {
				fillContentPanelWithEntities(foundEntity);
			} else {
				showNoResultsForEntity(foundEntity);
			}
		}
		
		layout(true);
	}

	private void fillContentPanelWithEntities(DimensionType foundEntity) {
		ContentPanel panel = dimensionPanels.get(foundEntity);
		panel.removeAll();
		
		panel.setHeading(I18N.fromEntities.getDimensionTypePluralName(foundEntity) + " (" +
				Integer.toString(affectedEntities.get(foundEntity).size()) +  ")");

		for (SearchResultEntity searchResultEntity : affectedEntities.get(foundEntity)) {
//			 Add every hit entity linked to it's landing page
//			Hyperlink link = new Hyperlink(searchResultEntity.getName(), "hm");
//			panelEntity.add(link);
			LabelField labelName = new LabelField("   " + searchResultEntity.getName() + "   ");
			panel.add(labelName, new RowData());
		}
	}

	private void showNoResultsForEntity(DimensionType foundEntity) {
		ContentPanel panel = dimensionPanels.get(foundEntity);
		panel.removeAll();
		panel.setHeading(I18N.fromEntities.getDimensionTypePluralName(foundEntity) + " (0)");
		LabelField labelNoMatches = new LabelField(I18N.CONSTANTS.noMatches());
		labelNoMatches.setStyleAttribute("color", "grey");
		panel.add(labelNoMatches);
	}

	public void setFilter(Map<DimensionType, List<SearchResultEntity>> affectedEntities) {
		this.affectedEntities = affectedEntities;

		updateUI();
	}
}
