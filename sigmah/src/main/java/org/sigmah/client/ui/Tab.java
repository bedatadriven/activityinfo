/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.ui;

/**
 * A simple tab displayable in a {@link TabBar}.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class Tab {
    private String title;
    private String action;
    private boolean closeable;

    public Tab() {
    }

    public Tab(String title, String action, boolean closeable) {
        this.title = title;
        this.action = action;
        this.closeable = closeable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.title != null ? this.title.hashCode() : 0);
        return hash;
    }
}
