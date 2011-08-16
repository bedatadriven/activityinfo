package org.sigmah.client.page.search;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.Image;


public class SearchFilterView extends LayoutContainer {
	private LabelField labelHeader;
	private Filter filter;
	private HorizontalPanel panelEntity;
	private HorizontalPanel panelNoResultsFound;
	private String searchQuery = "";
	
	public SearchFilterView() {
		super();
		
		addHeaderLabel();
		
		createNoResultsFoundPanel();
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
		add(labelHeader);
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
		
		updateUI();
	}
	
	public void setQuery(String query) {
		this.searchQuery=query;
	}


	private void updateUI() {
		if (panelEntity != null) {
			remove(panelEntity);
		}
		if (filter.hasRestrictions()) {
			showEntityPanel();
		} else {
			showNoResultsFound();
		}
	}


	private void showNoResultsFound() {
		labelHeader.setVisible(false);
		panelNoResultsFound.setVisible(true);
	}


	private void showEntityPanel() {		
		labelHeader.setVisible(true);
		panelNoResultsFound.setVisible(false);
		for (DimensionType foundEntity : filter.getRestrictedDimensions()) {
			panelEntity = new HorizontalPanel();
			
			Image iconEntityType = fromDimension(foundEntity);
			panelEntity.add(iconEntityType);
			
			LabelField labelEntityTypeCount = new LabelField();
			labelEntityTypeCount.setText(Integer.toString(filter.getRestrictions(foundEntity).size()));
			panelEntity.add(labelEntityTypeCount);
			
			LabelField labelEntityTypeName = new LabelField();
			labelEntityTypeName.setText(foundEntity.toString());
			panelEntity.add(labelEntityTypeName);
			
			LabelField labelEntityName = new LabelField();
			StringBuilder builder = new StringBuilder();
			for (Integer id : filter.getRestrictions(foundEntity)) {
				builder.append(Integer.toString(id));
				builder.append(", ");
			}
			
			String entitiesList = builder.toString();
			// Remove last ", "
			entitiesList = entitiesList.substring(0, entitiesList.length() -2);
			labelEntityName.setText(entitiesList);
			panelEntity.add(labelEntityName);
			
			// TODO: add a label with the names of the entities. We do not have those here (yet).
			
			add(panelEntity);
		}
		
		layout(true);
	}


	private Image fromDimension(DimensionType dimension) {
		switch(dimension) {
		case AdminLevel:
			return IconImageBundle.ICONS.delete().createImage();
		case Database:
			return IconImageBundle.ICONS.database().createImage();
		case Activity:
			return IconImageBundle.ICONS.activity().createImage();
		}
		
		// etc
		
		return IconImageBundle.ICONS.delete().createImage();
	}
	
}
