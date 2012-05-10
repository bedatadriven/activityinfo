package org.activityinfo.client.report.editor.pivotTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Maintains a list of dimensions applicable for the current element
 *
 */
public class DimensionTree implements HasReportElement<PivotTableReportElement> {

	private Dispatcher dispatcher;
	private ReportEventHelper events;
	
	private TreeStore<DimensionModel> store;
	private TreePanel<DimensionModel> treePanel;

	private Set<Integer> previouslyLoaded = Collections.emptySet();
	
	private PivotTableReportElement model;
	private DimensionModel geographyRoot;

	
	public DimensionTree(EventBus eventBus, Dispatcher dispatcher) {
		this.events = new ReportEventHelper(eventBus, this);
		this.events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				onModelChanged();
			}
		});
		this.dispatcher = dispatcher;
		
		this.store = new TreeStore<DimensionModel>();
		addDimension(DimensionType.Database, I18N.CONSTANTS.database());
		addDimension(DimensionType.Activity, I18N.CONSTANTS.activity());
		addDimension(DimensionType.Indicator, I18N.CONSTANTS.indicator());
		addDimension(DimensionType.Partner, I18N.CONSTANTS.partner());
		addDimension(DimensionType.Project, I18N.CONSTANTS.project());
		addTimeDimensions();		
		addGeographyRoot();
	
		treePanel = new TreePanel<DimensionModel>(store);
		treePanel.setBorders(true);
		treePanel.setCheckable(true);
		treePanel.setCheckNodes(TreePanel.CheckNodes.LEAF);
		treePanel.setCheckStyle(TreePanel.CheckCascade.NONE);
		treePanel.getStyle().setNodeCloseIcon(null);
		treePanel.getStyle().setNodeOpenIcon(null);
		treePanel.setStateful(true);
		treePanel.setDisplayProperty("name");

		/* enable drag and drop for dev */
		// TreePanelDragSource source = new TreePanelDragSource(treePanel);
		// source.setTreeSource(DND.TreeSource.LEAF);
		/* end enable drag and drop for dev */

		treePanel.setId("statefullavaildims");
		treePanel.collapseAll();

		treePanel.addListener(Events.CheckChange, new Listener<TreePanelEvent<DimensionModel>>() {

			@Override
			public void handleEvent(TreePanelEvent<DimensionModel> be) {
				updateModelAfterCheckChange(be);
			}
		});
	}

	private void addGeographyRoot() {
		geographyRoot = new DimensionModel(I18N.CONSTANTS.geography());
		store.add(geographyRoot, false);
		store.add(geographyRoot, new DimensionModel(DimensionType.Site, I18N.CONSTANTS.location()), false);
	}

	private void addDimension(DimensionType type, String name) {
		store.add(new DimensionModel(type, name), false);
	}
		
	private void addTimeDimensions() {
		DimensionModel folder = new DimensionModel(I18N.CONSTANTS.time());
		store.add(folder, false);
		store.add(folder, new DimensionModel(DateUnit.YEAR), false);
		store.add(folder, new DimensionModel(DateUnit.QUARTER), false);
		store.add(folder, new DimensionModel(DateUnit.MONTH), false);
	}
	
	@Override
	public void bind(PivotTableReportElement model) {
		this.model = model;
		
	}

	@Override
	public PivotTableReportElement getModel() {
		return model;
	}
	
	private void onModelChanged() {
		if(needToReloadDimensions(model)) {
			clearIndicatorSpecificDimensions();
			dispatcher.execute(new GetSchema(), 
					new MaskingAsyncMonitor(treePanel, I18N.CONSTANTS.loading()), new AsyncCallback<SchemaDTO>() {
	
				@Override
				public void onFailure(Throwable caught) {
					
				}
	
				@Override
				public void onSuccess(SchemaDTO result) {
					populateIndicatorSpecificDimensions(result);
					applyModelState();
				}
			});
		} 
	}
	
	private void applyModelState() {	
		for(DimensionModel node : store.getAllItems()) {
			if(node.hasDimension()) {
				treePanel.setChecked(node, 
						model.getRowDimensions().contains(node.getDimension()) ||
						model.getColumnDimensions().contains(node.getDimension()));
				
			}
		}
	}

	private boolean needToReloadDimensions(PivotTableReportElement model) {
		return !previouslyLoaded.containsAll(model.getIndicators()) ||
			   !model.getIndicators().containsAll(previouslyLoaded);
	}

	private void clearIndicatorSpecificDimensions() {
		
		for(DimensionModel model : Lists.newArrayList(store.getAllItems())) {
			if(model.hasDimension() && (
					model.getDimension() instanceof AttributeGroupDimension ||
					model.getDimension() instanceof AdminDimension)) {
				store.remove(model);
			}
		}
	}

	private void populateIndicatorSpecificDimensions(SchemaDTO schema) {
		
		addGeography(schema);
		addAttributeGroups(schema);
		previouslyLoaded = model.getIndicators();
	}

	private void addGeography(SchemaDTO schema) {
		
				
		Set<CountryDTO> countries = Sets.newHashSet();
		
		for(UserDatabaseDTO db : schema.getDatabases()) {
			for(ActivityDTO activity : db.getActivities()) {
				if(included(activity)) {
					countries.add(db.getCountry());
				}
			}
		}
		
		if(countries.size() == 1) {
			CountryDTO country = countries.iterator().next();
			for(AdminLevelDTO level : country.getAdminLevels()) {
				store.insert(geographyRoot, new DimensionModel(level), 
						store.getChildCount(geographyRoot)-1,
						false);
			}
		}
	}
	
	private void addAttributeGroups(SchemaDTO schema) {
		for(UserDatabaseDTO db : schema.getDatabases()) {
			for(ActivityDTO activity : db.getActivities()) {
				if(included(activity)) {
					for(AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {
						store.add(new DimensionModel(attributeGroup), false);
					}
				}
			}
		}
	}

	private boolean included(ActivityDTO activity) {
		Set<Integer> indicators = model.getIndicators();
		for(IndicatorDTO indicator : activity.getIndicators()) {
			if(indicators.contains(indicator.getId())) {
				return true;
			}
		}
		return false;
	}
	
	private void updateModelAfterCheckChange(TreePanelEvent<DimensionModel> event) {
		Dimension dim = event.getItem().getDimension();

		if(event.isChecked()) {
			if(!model.getRowDimensions().contains(dim) &&
			   !model.getColumnDimensions().contains(dim)) { 
				
				if (model.getRowDimensions().size() > model.getColumnDimensions().size()) {
					model.addColDimension(dim);
				} else {
					model.addRowDimension(dim);
				}
			}
		} else {
			model.getRowDimensions().remove(dim);
			model.getColumnDimensions().remove(dim);
		}		
		
		events.fireChange();
	}

	public Component asComponent() {
		return treePanel;
	}

	@Override
	public void disconnect() {
		events.disconnect();
	}
}
