

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

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.PageState;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.inject.Inject;

/**
 * UI component that provides a heirarchial
 * list of navigation links.
 *
 * To use, you must implement {@link org.activityinfo.client.page.common.nav.Navigator},
 * which provides the data for {@link com.extjs.gxt.ui.client.widget.treepanel.TreePanel}
 *
 */
public class NavigationPanel extends ContentPanel {
    private final EventBus eventBus;

    private TreePanel<Link> tree;

    private Listener<NavigationEvent> navListener;
    private Listener<BaseEvent> changeListener;

    @Inject
    public NavigationPanel(final EventBus eventBus, final Navigator navigator) {
        this.eventBus = eventBus;

        this.setHeading(navigator.getHeading());
        this.setScrollMode(Scroll.NONE);
        this.setLayout(new FitLayout());
        
        tree = new LinkTreePanel(navigator, navigator.getStateId());
        
        tree.addListener(Events.OnClick, new Listener<TreePanelEvent<Link>>() {
            @Override
            public void handleEvent(TreePanelEvent<Link> tpe) {
                if(tpe.getItem().getPageState() != null) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, tpe.getItem().getPageState()));
                }
            }
        });

        navListener = new Listener<NavigationEvent>() {
            @Override
			public void handleEvent(NavigationEvent be) {
                onNavigated(be.getPlace());
            }
        };
        eventBus.addListener(NavigationHandler.NavigationAgreed, navListener);


        changeListener = new Listener<BaseEvent>() {
            @Override
			public void handleEvent(BaseEvent be) {
            	tree.getStore().getLoader().load();
            }
        };
        eventBus.addListener(AppEvents.SCHEMA_CHANGED, changeListener);

        this.add(tree);
    }
    
    public void shutdown() {
        eventBus.removeListener(NavigationHandler.NavigationAgreed, navListener);
        eventBus.removeListener(AppEvents.SCHEMA_CHANGED, changeListener);
    }

    private void onNavigated(PageState place) {
        for(Link link : tree.getStore().getAllItems()) {
            if(link.getPageState() != null && link.getPageState().equals(place)) {
                ensureVisible(link);
            }
        }
    }

    public void ensureVisible(final Link link) {
        if(tree.isRendered()) {
            doExpandParents(link);
        } else {
            tree.addListener(Events.Render, new Listener<ComponentEvent>() {
                @Override
				public void handleEvent(ComponentEvent be) {
                    doExpandParents(link);
                    tree.removeListener(Events.Render, this);

                }
            });
        }
    }

    private void doExpandParents(Link link) {
        Link parent = tree.getStore().getParent(link);

        tree.getSelectionModel().select(link, false);

        while(parent != null) {
            tree.setExpanded(parent, true);
            parent = tree.getStore().getParent(parent);
        }
    }
}
