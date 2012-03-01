package org.sigmah.client.page.common.nav;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class LinkTreePanel extends TreePanel<Link> {

	public LinkTreePanel(Navigator navigator, String stateId) {
		super(createStore(navigator));
			    
	    setStateful(true);
	    setStateId(stateId);
	    setDisplayProperty("name");
	    setAutoLoad(true);
	    setIconProvider(new ModelIconProvider<Link>() {
	        @Override
	        public AbstractImagePrototype getIcon(Link model) {
	            return model.getIcon();
	        }
	    });
	}

	private static TreeStore<Link> createStore(final Navigator navigator) {
	  TreeLoader<Link> loader = new BaseTreeLoader<Link>(navigator) {
            @Override
            public boolean hasChildren(Link parent) {
                return navigator.hasChildren(parent);
            }
        };

        TreeStore<Link> store = new TreeStore<Link>(loader);
        store.setKeyProvider(new ModelKeyProvider<Link>() {
            @Override
            public String getKey(Link link) {
                return link.getKey();
            }
        });
        return store;
	}
	
	
}
