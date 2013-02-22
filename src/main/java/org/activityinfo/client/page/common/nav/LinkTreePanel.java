package org.activityinfo.client.page.common.nav;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
