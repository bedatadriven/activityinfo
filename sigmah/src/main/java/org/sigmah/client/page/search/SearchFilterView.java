package org.sigmah.client.page.search;

import java.util.List;
import java.util.Map;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.Image;


public class SearchFilterView extends LayoutContainer {
	private LabelField labelHeader;
	private VerticalPanel panelEntities;
	private HorizontalPanel panelNoResultsFound;
	private String searchQuery = "";
	private Map<DimensionType, List<SearchResultEntity>> affectedEntities;
	
	public SearchFilterView() {
		super();
		
		SearchResources.INSTANCE.searchStyles().ensureInjected();
		setStylePrimaryName("filterView");
		
		addHeaderLabel();
		
		createNoResultsFoundPanel();
		createEntityPanel();
	}

	private void createEntityPanel() {
		panelEntities = new VerticalPanel();
		panelEntities.setStylePrimaryName("filterView");
		add(panelEntities);
	}

	private void createNoResultsFoundPanel() {
		panelNoResultsFound = new HorizontalPanel();
		LabelField labelNoResultsFound = new LabelField();
		labelNoResultsFound.setText(I18N.MESSAGES.noSearchResults(searchQuery));
		add(panelNoResultsFound);
	}

	private void addHeaderLabel() {
		labelHeader = new LabelField();
		labelHeader.setText("Showing result for:");
		labelHeader.setStylePrimaryName("filterSummary");
		add(labelHeader);
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
		labelHeader.setVisible(hasFilter);
		panelEntities.setVisible(hasFilter);

		// hide no results panel
		panelNoResultsFound.setVisible(!hasFilter);
	}


	private void showEntityPanel() {		
		panelEntities.removeAll();
		panelEntities.setStylePrimaryName("filterView");
		for (DimensionType foundEntity : affectedEntities.keySet()) {
			HorizontalPanel panelEntity = new HorizontalPanel();
			
			Image iconEntityType = IconImageBundle.fromEntities.fromDimension(foundEntity).createImage();
			panelEntity.add(iconEntityType);
			
			LabelField labelEntityTypeCount = new LabelField();
			labelEntityTypeCount.setText(Integer.toString(affectedEntities.get(foundEntity).size()));
			panelEntity.add(labelEntityTypeCount);
			
			LabelField labelEntityTypeName = new LabelField();
			labelEntityTypeName.setText(I18N.fromEntities.getDimensionTypePluralName(foundEntity));
			labelEntityTypeName.addInputStyleName("font-weight:bold");
			panelEntity.add(labelEntityTypeName);

			HorizontalPanel panelEntityResults = new HorizontalPanel();
			panelEntityResults.setStylePrimaryName("panelEntityResults");
			panelEntityResults.setSpacing(10);
			
			// Add every hit entity linked to it's landing page
			for (SearchResultEntity searchResultEntity : affectedEntities.get(foundEntity)) {
//				Hyperlink link = new Hyperlink(searchResultEntity.getName(), "hm");
//				panelEntity.add(link);
				LabelField labelName = new LabelField(searchResultEntity.getName());
				panelEntityResults.add(labelName);
			}
			
			panelEntities.add(panelEntity);
			panelEntities.add(panelEntityResults);
		}
		
		layout(true);
	}

	public void setFilter(Map<DimensionType, List<SearchResultEntity>> affectedEntities) {
		this.affectedEntities = affectedEntities;

		updateUI();
	}
}
