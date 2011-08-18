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
import com.google.gwt.user.client.ui.Hyperlink;
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
			
			Image iconEntityType = fromDimension(foundEntity);
			panelEntity.add(iconEntityType);
			
			LabelField labelEntityTypeCount = new LabelField();
			labelEntityTypeCount.setText(Integer.toString(affectedEntities.get(foundEntity).size()));
			panelEntity.add(labelEntityTypeCount);
			
			LabelField labelEntityTypeName = new LabelField();
			labelEntityTypeName.setText(getEntityTypePluralName(foundEntity));
			panelEntity.add(labelEntityTypeName);

			// Add every hit entity linked to it's landing page
			for (SearchResultEntity searchResultEntity : affectedEntities.get(foundEntity)) {
				Hyperlink link = new Hyperlink(searchResultEntity.getName(), "hm");
				panelEntity.add(link);
			}
			
			panelEntity.setStylePrimaryName("filterView");
			panelEntities.add(panelEntity);
		}
		
		layout(true);
	}

	public void setFilter(Map<DimensionType, List<SearchResultEntity>> affectedEntities) {
		this.affectedEntities = affectedEntities;

		updateUI();
	}
	
	private String getEntityTypePluralName(DimensionType dimension) {
		switch (dimension) {
		case Activity:
			return I18N.CONSTANTS.activities();
		case AdminLevel:
			return I18N.CONSTANTS.adminEntities();
		case Partner:
			return I18N.CONSTANTS.partners();
		case Project:
			return I18N.CONSTANTS.projects();
		case AttributeGroup:
			return I18N.CONSTANTS.attributeTypes();
		case Indicator:
			return I18N.CONSTANTS.indicators();
		}
		return "No pluralized string definition in SearchFilterView.java";
	}

	private Image fromDimension(DimensionType dimension) {
		switch(dimension) {
		case AdminLevel:
			return IconImageBundle.ICONS.adminlevel1().createImage();
		case Database:
			return IconImageBundle.ICONS.database().createImage();
		case Activity:
			return IconImageBundle.ICONS.activity().createImage();
		case Project:
			return IconImageBundle.ICONS.project().createImage();
		case Partner:
			return IconImageBundle.ICONS.partner().createImage();
		case Indicator:
			return IconImageBundle.ICONS.indicator().createImage();
		}
		
		// etc
		
		return IconImageBundle.ICONS.delete().createImage();
	}
	
}
