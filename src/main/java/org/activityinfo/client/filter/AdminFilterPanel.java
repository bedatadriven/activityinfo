/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterHandler;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterHandler;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * UI Component for editing Admin restrictions on a {@link org.activityinfo.shared.command.Filter}
 *
 * @author Alex Bertram
 */
public class AdminFilterPanel extends ContentPanel implements FilterPanel {

    private final Dispatcher service;
    private TreeStore<AdminEntityDTO> store;
    private AdminTreeLoader loader;

    private TreePanel<AdminEntityDTO> tree;
    
    private Filter baseFilter = new Filter();
    private Filter value = new Filter();
    
    private FilterToolBar filterToolBar;

    @Inject
    public AdminFilterPanel(Dispatcher service) {
        this.service = service;

        initializeComponent();

        createAdminEntitiesTree();
        createFilterToolBar();
        
        loadData();
        
        layout(true);
    }

	private void loadData() {
		service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Failed to load admin entities", caught);
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				Set<Integer> activities = value.getRestrictions(DimensionType.Activity);
				if(!activities.isEmpty()) {
					loadCountry(result.getActivityById(activities.iterator().next()).getDatabase().getCountry());
				} else if(!result.getCountries().isEmpty()) {
					loadCountry(result.getCountries().iterator().next());
				}
				// TODO: support multiple countries!
			}
		});	
	}
	
	private void loadCountry(CountryDTO country) {
		loader.setCountry(country);
		loader.setFilter(baseFilter);
		loader.load();
	}

	private void createAdminEntitiesTree() {
		tree = new TreePanel<AdminEntityDTO>(store) {

			@Override
			protected String register(AdminEntityDTO m) {
				String result = super.register(m);
				
				// at this point we know the TreeNode has been created
				// so we can set the check state
				if(value.getRestrictions(DimensionType.AdminLevel).contains(m.getId())) {
					tree.setChecked(m, true);
				}
				
				return result;
			}
		};

        tree.setCheckable(true);
        tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
        tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);

        tree.setDisplayProperty("name");
        tree.getStyle().setNodeCloseIcon(null);
        tree.getStyle().setNodeOpenIcon(null);
        tree.setStateful(true);
        tree.addCheckListener(new CheckChangedListener<AdminEntityDTO>() {

			@Override
			public void checkChanged(CheckChangedEvent<AdminEntityDTO> event) {
				filterToolBar.setApplyFilterEnabled(!tree.getCheckedSelection().isEmpty());
			}
        });
        
        add(tree);
	}

    private void initializeComponent() {
        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeading(I18N.CONSTANTS.filterByGeography());
        this.setIcon(IconImageBundle.ICONS.filter());

        loader = new AdminTreeLoader(service);
        store = new TreeStore<AdminEntityDTO>(loader);
	}

	private void createFilterToolBar() {
		filterToolBar = new FilterToolBar();
		
		filterToolBar.addApplyFilterHandler(new ApplyFilterHandler() {
			@Override
			public void onApplyFilter(ApplyFilterEvent deleteEvent) {
				applyFilter();
			}
		});
		
		filterToolBar.addRemoveFilterHandler(new RemoveFilterHandler() {
			@Override
			public void onRemoveFilter(RemoveFilterEvent deleteEvent) {
				removeFilter();
			}
		});
		
		setTopComponent((Component) filterToolBar);
    }


    /**
     * @return the list of AdminEntityDTOs that user has selected with which
     * the filter should be restricted
     */
    public List<AdminEntityDTO> getSelection() {
        List<AdminEntityDTO> checked = tree.getCheckedSelection();
        List<AdminEntityDTO> selected = new ArrayList<AdminEntityDTO>();

        for (AdminEntityDTO entity : checked) {
            selected.add(entity);
        }
        return selected;
    }
    
	public void setSelection(int id, boolean select){
		
		for(ModelData model : tree.getStore().getAllItems()){
			if(model instanceof AdminEntityDTO && ((AdminEntityDTO) model).getId() == id){
				tree.setChecked((AdminEntityDTO) model, select);
			}			
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	
	private void applyFilter() {
		List<AdminEntityDTO> selection = getSelection();
		value = new Filter();
		for(AdminEntityDTO entity : selection) {
			value.addRestriction(DimensionType.AdminLevel, entity.getId());
		}
		ValueChangeEvent.fire(this, value);
		filterToolBar.setRemoveFilterEnabled(true);
	}

	private void removeFilter() {
		
		for(AdminEntityDTO entity : tree.getCheckedSelection()) {
			tree.setChecked(entity, false);
		}
			
		value = new Filter();
		ValueChangeEvent.fire(this, value);
		filterToolBar.setRemoveFilterEnabled(false);
	}
	
	@Override
	public Filter getValue() {
		return value;
	}

	@Override
	public void setValue(Filter value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		this.value = new Filter();
		this.value.addRestriction(DimensionType.AdminLevel, value.getRestrictions(DimensionType.AdminLevel));
		applyInternalState();
		
		filterToolBar.setApplyFilterEnabled(false);
		filterToolBar.setRemoveFilterEnabled(value.isRestricted(DimensionType.AdminLevel));
		
		if(fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public void applyBaseFilter(final Filter providedFilter) {
		Filter filter = new Filter(providedFilter);
		filter.clearRestrictions(DimensionType.AdminLevel);
		
		if(baseFilter == null || !baseFilter.equals(filter)) {
			baseFilter = filter;
			loader.setFilter(baseFilter);
			loader.load();
		}
	}
	
	private void applyInternalState() {
		for(AdminEntityDTO treeNode : tree.getStore().getAllItems()) {
			tree.setChecked(treeNode, value.getRestrictions(DimensionType.AdminLevel).contains(treeNode.getId()));
		}
	}
	
}
