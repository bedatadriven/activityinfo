/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
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
    
    private Filter baseFilter = new Filter();
    private Filter value = new Filter();
	private Button applyButton;
	private Button removeButton;
	
	private Set<Integer> selected = new HashSet<Integer>();

    public AdminFilterPanel(Dispatcher service) {
        this.service = service;

        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.AUTO);
        this.setHeading(I18N.CONSTANTS.filterByGeography());
        this.setIcon(IconImageBundle.ICONS.filter());


        loader = new AdminTreeLoader(service);
        store = new TreeStore<AdminEntityDTO>(loader);

        tree = new TreePanel<AdminEntityDTO>(store);

        tree.setCheckable(true);
        tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
        tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);

        tree.setDisplayProperty("name");
        tree.getStyle().setNodeCloseIcon(null);
        tree.getStyle().setNodeOpenIcon(null);
        tree.addCheckListener(new CheckChangedListener<AdminEntityDTO>() {

			@Override
			public void checkChanged(CheckChangedEvent<AdminEntityDTO> event) {
				applyButton.setEnabled(!tree.getCheckedSelection().isEmpty());
			}
        });
        
        tree.addListener(Events.Expand, new Listener<TreePanelEvent>() {

			@Override
			public void handleEvent(TreePanelEvent be) {
				onExpanded(be.getItem());
			}
        	
        });
        
        add(tree);

        createApplyBar();
        
    }

    private void createApplyBar() {
    	ToolBar bar = new ToolBar();
    	applyButton = new Button("Apply", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				applyFilter();
			}

		});
    	applyButton.disable();
		bar.add(applyButton);
    	removeButton = new Button(I18N.CONSTANTS.remove(), IconImageBundle.ICONS.delete(), new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				removeFilter();
			}
		});
		bar.add(removeButton);
		removeButton.disable();
    	setTopComponent(bar);
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
		value = new Filter();
		for(AdminEntityDTO entity : selection) {
			value.addRestriction(DimensionType.AdminLevel, entity.getId());
		}
		ValueChangeEvent.fire(this, value);
		removeButton.enable();
	}

	private void removeFilter() {
		
		for(AdminEntityDTO entity : tree.getCheckedSelection()) {
			tree.setChecked(entity, false);
		}
			
		value = new Filter();
		ValueChangeEvent.fire(this, value);
		removeButton.disable();
	}
	
	@Override
	public Filter getValue() {
		return value;
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
		if(!this.baseFilter.equals(filter)) {
			this.baseFilter = filter;
			final Set<Integer> activities = filter.getRestrictions(DimensionType.Activity);
			if(!activities.isEmpty()) {
				service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
	
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
	
					@Override
					public void onSuccess(SchemaDTO result) {
						loader.setCountry(result.getActivityById(activities.iterator().next()).getDatabase().getCountry());
						loader.setFilter(filter);
						loader.load();
					}
				
				});	
			}
		}
	}
	

	private void onExpanded(ModelData item) {
		
	}
}
