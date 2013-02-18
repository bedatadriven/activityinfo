/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.SuccessCallback;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;
import org.activityinfo.shared.dto.ProvidesKey;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckCascade;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * UI Component that allows the user to select a list of Indicators
 * 
 * @author Alex Bertram
 */
public class IndicatorTreePanel extends ContentPanel {

	private final Dispatcher dispatcher;

	private final TreeStore<ModelData> store;
	private final TreePanel<ModelData> tree;
	private ToolBar toolBar;
	private StoreFilterField filter;
	private boolean multipleSelection;
	
	/**
	 * Keep our own copy of our selection state that is indepedent of the 
	 * loading process
	 */
	private Set<Integer> selection = Sets.newHashSet();
	
	public IndicatorTreePanel(Dispatcher dispatcher,
			final boolean multipleSelection) {
		this.dispatcher = dispatcher;

		this.setHeading(I18N.CONSTANTS.indicators());
		this.setIcon(IconImageBundle.ICONS.indicator());
		this.setLayout(new FitLayout());
		this.setScrollMode(Style.Scroll.NONE);

		store = new TreeStore<ModelData>(new Loader());
		
		setStoreKeyProvider();
		
		tree = new TreePanel<ModelData>(store);

		tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
		tree.setCheckStyle(CheckCascade.CHILDREN);
		
		tree.getStyle().setNodeCloseIcon(null);
		tree.getStyle().setNodeOpenIcon(null);
		
		tree.setLabelProvider(new NodeLabelProvider());
				
		tree.setCheckable(true);
		tree.expandAll();
		tree.setStateId("indicatorPanel");
		tree.setStateful(true);
		tree.setAutoSelect(true);		
		tree.addListener(Events.BrowserEvent,
				new Listener<TreePanelEvent<ModelData>>() {

					@Override
					public void handleEvent(TreePanelEvent<ModelData> be) {
						if (be.getEventTypeInt() == Event.ONKEYPRESS) {
							if (!toolBar.isVisible()) {
								toolBar.setVisible(true);
							}
							filter.focus();
						}
					}
				});

		add(tree);
		createFilterBar();
		
		tree.getStore().addStoreListener(new StoreListener<ModelData>(){

			@Override
			public void storeDataChanged(StoreEvent<ModelData> se) {
				// apply our internal state to the newly loaded list
				applySelection();
			}
		});
		
		addCheckChangedListener(new Listener<TreePanelEvent>() {
			
			@Override
			public void handleEvent(TreePanelEvent be) {
				if(be.getItem() instanceof IndicatorDTO) {
					IndicatorDTO indicator = (IndicatorDTO)be.getItem();
					if(be.isChecked()) {
						selection.add(indicator.getId());
					} else {
						selection.remove(indicator.getId());
					}
				}
			}
		});
	}
	
	private void setStoreKeyProvider(){
		store.setKeyProvider(new ModelKeyProvider<ModelData>() {

			@Override
			public String getKey(ModelData model) {
				new ArrayList<String>();
				if (model instanceof ProvidesKey) {
					return ((ProvidesKey) model).getKey();
				} else if (model == null) {
					throw new IllegalStateException(
							"Did not expect model to be null: assigning keys in IndicatorTreePanel");
				}
				throw new IllegalStateException(
						"Unknown type: expected activity, userdb, indicator or indicatorgroup");
			}
		});
	}
	
	public void loadSingleDatabase(UserDatabaseDTO database){
		store.removeAll();
		for(ActivityDTO activity: database.getActivities()){
			store.add(activity, true);
			List<ModelData> models = createActivityChildren(activity);
			for(ModelData model : models){
				if(model instanceof IndicatorGroup){
					store.add(activity, model, true);
					store.add(model,createIndicatorList((IndicatorGroup)model), true);
				}else{
					store.add(activity, model, true);
				}
			}
			
		}
		tree.expandAll();
	}
	
	@Override
	public void setHeading(String heading){
		super.setHeading(heading);
	}
	
	private void createFilterBar() {
		toolBar = new ToolBar();
		toolBar.add(new LabelToolItem(I18N.CONSTANTS.search()));
		filter = new FilterField();
		filter.addListener(Events.Blur, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				if (filter.getRawValue() == null
						|| filter.getRawValue().length() == 0) {
					toolBar.setVisible(false);
				}
			}
		});
		toolBar.add(filter);
		toolBar.setVisible(false);
		filter.bind(store);
		setTopComponent(toolBar);
	}

	private final class NodeLabelProvider implements
			ModelStringProvider<ModelData> {
		@Override
		public String getStringValue(ModelData model, String property) {
			String name = model.get("name");
			if (model instanceof IndicatorDTO) {
				return name;
			} else {
				if (name == null) {
					name = "noname";
				}
				return "<b>" + name + "</b>";
			}
		}
	}

	private class Proxy implements DataProxy<List<ModelData>> {
		private SchemaDTO schema;

		@Override
		public void load(DataReader<List<ModelData>> listDataReader,
				Object parent, final AsyncCallback<List<ModelData>> callback) {

			if (parent == null) {
				dispatcher.execute(new GetSchema(), new MaskingAsyncMonitor(IndicatorTreePanel.this, I18N.CONSTANTS.loading()),
						new SuccessCallback<SchemaDTO>() {
				
					@Override
					public void onSuccess(SchemaDTO result) {
						schema = result;
						callback.onSuccess(new ArrayList<ModelData>(schema.getDatabases()));
					}
				});
			} else if (parent instanceof UserDatabaseDTO) {
				callback.onSuccess(new ArrayList<ModelData>(
						((UserDatabaseDTO) parent).getActivities()));

			} else if (parent instanceof ActivityDTO) {
				ActivityDTO acitvity = (ActivityDTO)parent;
				callback.onSuccess(createActivityChildren(acitvity));
				
			} else if (parent instanceof IndicatorGroup) {
				IndicatorGroup group = ((IndicatorGroup) parent);
				callback.onSuccess(createIndicatorList(group));
			}
		}
	}
	
	private List<ModelData> createActivityChildren(ActivityDTO acitvity){
		List<IndicatorGroup> groupIndicators = acitvity.groupIndicators();
		List<ModelData> children = new ArrayList<ModelData>();
		for (IndicatorGroup group : groupIndicators) {
			if (group.getName() == null) {
				for (IndicatorDTO indicator : group.getIndicators()) {
					children.add(indicator);
				}
			} else {
				children.add(group);
			}
		}
		
		return children;
	}
	
	private List<ModelData> createIndicatorList(IndicatorGroup group){		
		ArrayList<ModelData> list = new ArrayList<ModelData>();
		for (IndicatorDTO indicator : group.getIndicators()) {
			list.add(indicator);
		}
		
		return list;
	}


	/**
	 * @return the list of selected indicators
	 */
	public List<IndicatorDTO> getSelection() {
		List<IndicatorDTO> list = new ArrayList<IndicatorDTO>();
		for (ModelData model : tree.getCheckedSelection()) {
			if (model instanceof IndicatorDTO) {
				list.add((IndicatorDTO) model);
			}
		}
		return list;
	}
	
	public void select(int indicatorId, boolean select) {

		if(select) {
			if(!multipleSelection) {
				selection.clear();
			}
			selection.add(indicatorId);
		} else {
			selection.remove(indicatorId);
		}
		
		// apply directly if the indicators are loaded
		for(ModelData model : tree.getStore().getAllItems()){
			if(model instanceof IndicatorDTO && ((IndicatorDTO) model).getId() == indicatorId){
				setChecked((IndicatorDTO)model,select);		
			}			
		}
	}
	
	public void setSelection(Iterable<Integer> indicatorIds) {
		selection = Sets.newHashSet(indicatorIds);
		applySelection();
	}

	public void addCheckChangedListener(Listener<TreePanelEvent> listener) {
		tree.addListener(Events.CheckChange, listener);
	}
	
	public void addBeforeCheckedListener(Listener<TreePanelEvent> listener) {
		tree.addListener(Events.BeforeCheckChange, listener);
	}

	/**
	 * 
	 * @return the list of the ids of selected indicators
	 */
	public List<Integer> getSelectedIds() {
		return Lists.newArrayList(selection);
	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
		if (multipleSelection) {
			tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);
		} else {
			tree.setCheckStyle(TreePanel.CheckCascade.NONE);
		}
	}

	private class Loader extends BaseTreeLoader<ModelData> {
		public Loader() {
			super(new Proxy());
		}

		@Override
		public boolean hasChildren(ModelData parent) {
			return !(parent instanceof IndicatorDTO);
		}
	}

	private static class FilterField extends StoreFilterField {
		@Override
		protected boolean doSelect(Store store, ModelData parent,
				ModelData record, String property, String filter) {

			String keywords[] = filter.toLowerCase().split("\\s+");
			String name = ((String) record.get("name")).toLowerCase();
			for (String keyword : keywords) {
				if (name.indexOf(keyword) == -1) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean isMultipleSelection() {
		return multipleSelection;
	}

	public void clearSelection() {
		for (IndicatorDTO indicator : getSelection()) {
			tree.getSelectionModel().deselect(indicator);
			tree.setChecked(indicator, false);
		}
	}

	public void setChecked(IndicatorDTO indicator, boolean b) {
		tree.setExpanded(indicator, b);
		tree.setChecked(indicator, b);
	}
	
	public void setLeafCheckableOnly() {
		tree.setCheckNodes(TreePanel.CheckNodes.LEAF);
	}
	
	private void applySelection() {
		for(ModelData model : tree.getStore().getAllItems()){
			if(model instanceof IndicatorDTO) {
				IndicatorDTO indicator = (IndicatorDTO)model;
				if(model instanceof IndicatorDTO) {
					setChecked(indicator, selection.contains(indicator.getId()));		
				}
			}
		}
	}
}
