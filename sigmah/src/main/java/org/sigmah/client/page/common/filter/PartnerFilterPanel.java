/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.ProvidesKeyProvider;
import org.sigmah.client.page.common.CheckBoxList;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterHandler;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterHandler;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * UI Component that allows the user to choose a list of partners
 * on which to restrict the query.
 */
public class PartnerFilterPanel extends ContentPanel implements FilterPanel {
	private TreeLoader<PartnerDTO> loader;
	private TreeStore<PartnerDTO> store;
	private CheckBoxList<PartnerDTO> tree;
	private final Dispatcher service;
	private FilterToolBar filterToolBar;
	
	@Inject
    public PartnerFilterPanel(Dispatcher service) {
    	this.service = service;
    	
        initializeComponent();

        createFilterToolBar();
        createLoader();
        createStore();
        createTree();
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

	protected void removeFilter() {
		// TODO: implement
	}

	protected void applyFilter() {
		// TODO: implement
	}

	private void createTree() {
		tree = new CheckBoxList<PartnerDTO>(store);
        add(tree);
	}

	private void createStore() {
		store = new TreeStore<PartnerDTO>(loader);
        
        store.setKeyProvider(new ProvidesKeyProvider<PartnerDTO>());
	}

	private void createLoader() {
		loader = new BaseTreeLoader<PartnerDTO>(new PartnerDataProxy()) {
        	@Override
        	public boolean hasChildren(PartnerDTO parent) {
        		return false;
        	}
        };
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
        
        setLayout(new FitLayout());
        setScrollMode(Style.Scroll.NONE);
        setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
	}
  
	private class PartnerDataProxy implements DataProxy<List<PartnerDTO>> {

        private List<PartnerDTO> allPartners = null;

        public void load(DataReader<List<PartnerDTO>> listDataReader, Object o, final AsyncCallback<List<PartnerDTO>> callback) {
           
            if (allPartners == null) {
                service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    public void onSuccess(SchemaDTO result) {
                        List<UserDatabaseDTO> databases = result.getDatabases();
        				ArrayList<PartnerDTO> allPartners = new ArrayList<PartnerDTO>();
        				for (UserDatabaseDTO db : databases) {
        					for ( PartnerDTO p : db.getPartners()) {
	        					if (!allPartners.contains(p)) {
	        						allPartners.add(p);
	        					}
        					}
        				}
        				Collections.sort(allPartners,new PartnerDTOComparator());
                        callback.onSuccess(new ArrayList<PartnerDTO>(allPartners));
                    }
                });
            } else {
                callback.onSuccess(new ArrayList<PartnerDTO>(allPartners));
            }
        }
    }
	
	private class PartnerDTOComparator implements Comparator{
		
		public int compare(Object partner1, Object partner2){
			String name1= null,name2=null;
			if (partner1 instanceof PartnerDTO) {
				 name1 = ((PartnerDTO)partner1).getName();
			} 
			if (partner2 instanceof PartnerDTO) {
				 name2 = ((PartnerDTO)partner2).getName();
			}
			if (name1 == null) {
				name1 = "";
			}
			if (name2 == null) {
				name2 = "";
			}
			return name1.compareTo(name2);
		}
	}

    public List<PartnerDTO> getSelection() {
        List<PartnerDTO> list = new ArrayList<PartnerDTO>();

        for (ModelData model : tree.getCheckedSelection()) {
        	if (model instanceof PartnerDTO) {
        		list.add((PartnerDTO) model);
        	}
        }
        return list;
    }
    
    public List<Integer> getSelectedIds() {
        List<Integer> list = new ArrayList<Integer>();

        for (ModelData model : tree.getCheckedSelection()) {
            if (model instanceof PartnerDTO) {
                list.add(((PartnerDTO) model).getId());
            }
        }
        return list;
    }

	@Override
	public Filter getValue() {
		Filter filter = new Filter();
		List<Integer> selectedIds = getSelectedIds();
		if (selectedIds.size() > 0) {
			filter.addRestriction(DimensionType.Partner, getSelectedIds());
		}
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void applyBaseFilter(Filter filter) {
		filter.addRestriction(DimensionType.Partner, getSelectedIds());
	}
}
