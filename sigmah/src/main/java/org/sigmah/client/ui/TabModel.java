/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

/**
 * Maintains a list of tabs.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Singleton
public class TabModel {
    public static interface Listener {
        void tabAdded(Tab t);
        void tabRemoved(int index);
        void tabChanged(int index);
    }
    
    private final ArrayList<Tab> tabs = new ArrayList<Tab>();
    private final ArrayList<Listener> listeners = new ArrayList<Listener>();
    
    /**
     * Add a new tab to the tab bar.
     * If the tab with the same PageState.pageId already exists, it is updated to reflect the new given values.
     * The closeable state cannot be changed once it has been set.
     * @param title Title to display in the tab bar. <code>null</code> displays a loading message.
     * @param state State to restore when the tab is selected. The PageId value is used as a primary key.
     * @param closeable
     * @return
     */
    public Tab add(String title, PageState state, boolean closeable) {
        final String displayedTitle;
        if(title == null)
            displayedTitle = I18N.CONSTANTS.loading();
        else
            displayedTitle = title;
        
        final Tab t = new Tab(displayedTitle, state, closeable);
        
        int index = tabs.indexOf(t);
        if(index != -1) {
            // Updating the tab
            final Tab tab = tabs.get(index);
            if(state != null)
                tab.setState(state);
            if(title != null && !title.equals(tab.getTitle()))
                tab.setTitle(title);
            
            tab.setCloseable(closeable);
            fireTabChanged(index);
        } else {
            // New tab
            t.addListener(new Tab.Listener() {
                @Override
                public void titleChanged(String title) {
                    final int index = tabs.indexOf(t);
                    if(index != -1)
                        fireTabChanged(index);
                }
            });
            
            tabs.add(t);
            fireTabAdded(t);
        }
        
        return t;
    }
    
    public Tab get(int index) {
        return tabs.get(index);
    }
    
    public Tab get(PageState state) {
        final Tab fakeTab = new Tab(null, state, false);
        final int index = tabs.indexOf(fakeTab);
        if(index != -1)
            return tabs.get(index);
        else
            return null;
    }
    
    public int size() {
        return tabs.size();
    }
    
    public int indexOf(Tab t) {
        return tabs.indexOf(t);
    }
    
    public void remove(Tab t) {
        final int index = tabs.indexOf(t);
        tabs.remove(index);
        
        fireTabRemoved(index);
    }
    
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    protected void fireTabAdded(final Tab tab) {
        for(Listener listener : listeners) {
            listener.tabAdded(tab);
        }
    }
    
    protected void fireTabRemoved(final int index) {
        for(Listener listener : listeners) {
            listener.tabRemoved(index);
        }
    }
    
    protected void fireTabChanged(final int index) {
        for(Listener listener : listeners) {
            listener.tabChanged(index);
        }
    }
}
