package org.sigmah.client.page.common.filter;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * UI Component that allows the user to choose a list of partens
 * on which to restrict the query.
 *
 * @author JDH
 */
public class PartnerFilterPanel extends ContentPanel {
	private TreeLoader<ModelData> loader;
	private TreeStore<ModelData> store;
	private TreePanel<ModelData> tree;
	private final Dispatcher service;	
	
    public PartnerFilterPanel(Dispatcher service) {
    	this.service = service;
        setHeading(I18N.CONSTANTS.filterByPartner());
        setIcon(IconImageBundle.ICONS.filter());
        
        this.setLayout(new FitLayout());
        this.setScrollMode(Style.Scroll.NONE);
        this.setHeading(I18N.CONSTANTS.filterByPartner());
        this.setIcon(IconImageBundle.ICONS.filter());

        loader = new BaseTreeLoader<ModelData>(new PartnerDataProxy()) {
        	@Override
        	public boolean hasChildren(ModelData parent) {
        		return false;
        	}
        };
        
        store = new TreeStore<ModelData>(loader);
        
        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
            @Override
            public String getKey(ModelData model) {
            	if (model instanceof PartnerDTO) {
            		return "" + ((PartnerDTO)model).getId();
            	} else {
            		return "";
            	}
            }
        });

        tree = new TreePanel<ModelData>(store);
        tree.setCheckable(true);
        tree.setCheckNodes(TreePanel.CheckNodes.LEAF);
        tree.setCheckStyle(TreePanel.CheckCascade.NONE);
        tree.getStyle().setNodeCloseIcon(null);
        tree.getStyle().setNodeOpenIcon(null);
        tree.setBorders(true);
        tree.setDisplayProperty("name");
 
        add(tree);
    }
  
	private class PartnerDataProxy implements DataProxy<List<ModelData>> {

        private List<PartnerDTO> allPartners = null;

        public void load(DataReader<List<ModelData>> listDataReader, Object o, final AsyncCallback<List<ModelData>> callback) {
           
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
                        callback.onSuccess(new ArrayList<ModelData>(allPartners));
                    }
                });
            } else {
                callback.onSuccess(new ArrayList<ModelData>(allPartners));
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
}
