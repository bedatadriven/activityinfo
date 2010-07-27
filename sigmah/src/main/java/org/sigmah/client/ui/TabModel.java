/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import java.util.ArrayList;

/**
 * Maintains a list of tabs.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class TabModel {
    public static interface Listener {
        void tabAdded(Tab t);
        void tabRemoved(int index);
        void tabChanged(int index);
    }
    
    private final ArrayList<Tab> tabs = new ArrayList<Tab>();
    private final ArrayList<Listener> listeners = new ArrayList<Listener>();
    
    public Tab add(String title, String action, boolean closeable) {
        final Tab t = new Tab(title, action, closeable);
        
        int index = tabs.indexOf(t);
        if(index != -1) {
            tabs.get(index).setAction(action);
            tabs.get(index).setCloseable(closeable);
            fireTabChanged(index);
        } else {
            tabs.add(t);
            fireTabAdded(t);
        }
        
        return t;
    }
    
    public Tab get(int index) {
        return tabs.get(index);
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
