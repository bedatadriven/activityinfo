/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.nav;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageState;

/**
 * UI component that provides a hierarchial
 * list of navigation links.
 *
 * To use, you must implement {@link org.sigmah.client.page.common.nav.Navigator},
 * which provides the data for {@link com.extjs.gxt.ui.client.widget.treepanel.TreePanel}
 *
 */
public class NavigationPanel extends ContentPanel {
    protected final EventBus eventBus;

    protected TreePanel<Link> tree;
    protected TreeLoader<Link> loader;
    protected TreeStore<Link> store;

    protected Listener<NavigationEvent> navListener;
    protected Listener<BaseEvent> changeListener;

    public NavigationPanel(final EventBus eventBus, final Navigator navigator) {
        this.eventBus = eventBus;

        this.setHeading(navigator.getHeading());
        this.setScrollMode(Scroll.NONE);
        this.setLayout(new FitLayout());
        
        loader = new BaseTreeLoader<Link>(navigator) {
            @Override
            public boolean hasChildren(Link parent) {
                return navigator.hasChildren(parent);
            }
        };

        store = new TreeStore<Link>(loader);
        store.setKeyProvider(new ModelKeyProvider<Link>() {
            @Override
            public String getKey(Link link) {
                return link.getKey();
            }
        });
        
        tree = new TreePanel<Link>(store);
        tree.setStateful(true);
        tree.setStateId(navigator.getStateId());
        tree.setDisplayProperty("name");
        tree.setAutoLoad(true);
        tree.setIconProvider(new ModelIconProvider<Link>() {
            @Override
            public AbstractImagePrototype getIcon(Link model) {
                return model.getIcon();
            }
        });

        tree.addListener(Events.OnClick, new Listener<TreePanelEvent<Link>>() {
            @Override
            public void handleEvent(TreePanelEvent<Link> tpe) {
                if(tpe.getItem().getPageState() != null) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, tpe.getItem().getPageState()));
                }
            }
        });

        navListener = new Listener<NavigationEvent>() {
            public void handleEvent(NavigationEvent be) {
                onNavigated(be.getPlace());
            }
        };
        eventBus.addListener(NavigationHandler.NavigationAgreed, navListener);


        changeListener = new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                loader.load();
            }
        };
        eventBus.addListener(AppEvents.SchemaChanged, changeListener);

        this.add(tree);
       
    }
    
    public void shutdown() {
        eventBus.removeListener(NavigationHandler.NavigationAgreed, navListener);
        eventBus.removeListener(AppEvents.SchemaChanged, changeListener);
    }

    private void onNavigated(PageState place) {
        for(Link link : store.getAllItems()) {
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
                public void handleEvent(ComponentEvent be) {
                    doExpandParents(link);
                    tree.removeListener(Events.Render, this);

                }
            });
        }
    }

    private void doExpandParents(Link link) {
        Link parent = store.getParent(link);

        tree.getSelectionModel().select(link, false);

        while(parent != null) {
            tree.setExpanded(parent, true);
            parent = store.getParent(parent);
        }
    }
}
