/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.ui;

import com.google.gwt.user.client.ui.HTML;

/**
 * Simple anchor that can be turned into a label and vice versa.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ToggleAnchor extends HTML {
    private String label;
    private String target;
    private boolean anchorMode;

    /**
     * Creates a new ToggleAnchor and initialize its target to "<code>javascript:;</code>".
     * @param html Label of the new link in HTML format
     */
    public ToggleAnchor(String html) {
        this.label = html;
        this.target = "javascript:;";
        this.anchorMode = false;
    }

    /**
     * Returns the current label of this anchor.
     * @return The current label in HTML format.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Defines the label of this anchor.
     * @param label The new value to display.
     */
    public void setLabel(String label) {
        this.label = label;
        setAnchorMode(anchorMode);
    }

    /**
     * Retrieves the current target URL of this anchor.
     * @return The target URL in String.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Defines the target URL of this anchor.
     * @param target The new URL in String.
     */
    public void setTarget(String target) {
        this.target = target;
        setAnchorMode(anchorMode);
    }

    /**
     * Tells wether or not this widget is in anchor mode.
     * @return <code>true</code> if this widget is a link, <code>false</code> otherwise.
     */
    public boolean isAnchorMode() {
        return anchorMode;
    }

    /**
     * Changes the anchor mode of this widget.
     * @param anchorMode <code>true</code> to display an anchor, <code>false</code> otherwise.
     */
    public void setAnchorMode(boolean anchorMode) {
        this.anchorMode = anchorMode;

        if(anchorMode) {
            setHTML("<a href=\""+target+"\">"+label+"</a>");
        } else {
            setHTML(label);
        }
    }

    /**
     * Reverse the anchor mode of this widget.
     */
    public void toggleAnchorMode() {
        setAnchorMode(!this.anchorMode);
    }
}
