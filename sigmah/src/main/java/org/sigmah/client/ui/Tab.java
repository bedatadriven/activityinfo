/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

import java.util.ArrayList;
import org.sigmah.client.page.PageState;

/**
 * A simple tab displayable in a {@link TabBar}.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class Tab {
    private String title;
    private PageState state;
    private boolean closeable;
    
    private final ArrayList<Listener> listeners = new ArrayList<Listener>();

    public static interface Listener {
        void titleChanged(String title);
    }
    
    public Tab() {
    }

    public Tab(String title, PageState state, boolean closeable) {
        this.title = title;
        this.state = state;
        this.closeable = closeable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        fireTitleChanged(title);
    }

    public PageState getState() {
        return state;
    }

    public void setState(PageState state) {
        this.state = state;
    }

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tab other = (Tab) obj;
        if (this.state != other.state && (this.state == null || !this.state.getPageId().equals(other.state.getPageId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        
        final int pageIdHash;
        if(this.state != null && this.state.getPageId() != null)
            pageIdHash = this.state.getPageId().toString().hashCode();
        else
            pageIdHash = 0;
        
        hash = 17 * hash + pageIdHash;
        return hash;
    }
    
    /**
     * Register a new listener for changes in this tab.
     * @param listener The new listener.
     */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes the given listener from the list of registered listeners.
     * If '<code>listener</code>' isn't registered, nothing is done.
     * @param listener The listener to remove.
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify every registered listener that the title of this tab has changed.
     * @param title The new title.
     */
    protected void fireTitleChanged(String title) {
        for(Listener listener : listeners)
            listener.titleChanged(title);
    }
}
