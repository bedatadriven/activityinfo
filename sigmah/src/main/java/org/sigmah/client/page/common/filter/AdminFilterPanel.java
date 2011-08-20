/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterHandler;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterHandler;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
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

/**
 * UI Component for editing Admin restrictions on a {@link org.sigmah.shared.dao.Filter}
 *
 * @author Alex Bertram
 */
public class AdminFilterPanel extends ContentPanel implements FilterPanel {

    private final Dispatcher service;
    private TreeStore<AdminEntityDTO> store;
    private AdminTreeLoader loader;

    private TreePanel<AdminEntityDTO> tree;
    
    private Filter baseFilter = null;
    private Filter filter = new Filter();
    
    private FilterToolBar filterToolBar;

    public AdminFilterPanel(Dispatcher service) {
        this.service = service;

        initializeComponent();

        createAdminEntitiesTree();
        createFilterToolBar();
        
        layout(true);
    }

	private void createAdminEntitiesTree() {
		tree = new TreePanel<AdminEntityDTO>(store);

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
		filterToolBar = new FilterToolBarImpl();
		
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

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	

	private void applyFilter() {
		List<AdminEntityDTO> selection = getSelection();
		filter = new Filter();
		for(AdminEntityDTO entity : selection) {
			filter.addRestriction(DimensionType.AdminLevel, entity.getId());
		}
		ValueChangeEvent.fire(this, filter);
		filterToolBar.setRemoveFilterEnabled(true);
	}

	private void removeFilter() {
		
		for(AdminEntityDTO entity : tree.getCheckedSelection()) {
			tree.setChecked(entity, false);
		}
			
		filter = new Filter();
		ValueChangeEvent.fire(this, filter);
		filterToolBar.setRemoveFilterEnabled(false);
	}
	
	@Override
	public Filter getValue() {
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyBaseFilter(final Filter filter) {
		if(baseFilter == null || !baseFilter.equals(filter)) {
			baseFilter = filter;
			service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Failed to load admin entities", caught);
				}

				@Override
				public void onSuccess(SchemaDTO result) {
					Set<Integer> activities = filter.getRestrictions(DimensionType.Activity);
					if(!activities.isEmpty()) {
						loader.setCountry(result.getActivityById(activities.iterator().next()).getDatabase().getCountry());
					} else if(!result.getCountries().isEmpty()) {
						loader.setCountry(result.getCountries().iterator().next());
					} else {
						// TODO: support multiple countries!
					}
					loader.setFilter(filter);
					loader.load();
				}
			});	
		}
	}
	
}
