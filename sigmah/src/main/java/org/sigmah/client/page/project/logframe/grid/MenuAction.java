package org.sigmah.client.page.project.logframe.grid;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Defines a menu action.
 * 
 * @author tmi
 * 
 */
public abstract class MenuAction {

    /**
     * Defines the different types of policies that the menu can follow to
     * inactive actions.
     * 
     * @author tmi
     * 
     */
    public static enum InactivationPolicy {

        /**
         * The inactive action will be disabled.
         */
        DISABLE_POLICY,

        /**
         * The inactive action will be hidden.
         */
        HIDE_POLICY;
    }

    /**
     * The menu item symbolizing this action.
     */
    private final MenuItem item;

    /**
     * The inactivation policy.
     */
    private InactivationPolicy inactivationPolicy;

    /**
     * Builds this action.
     */
    public MenuAction() {

        item = new MenuItem(getText(), getIcon());

        // Action.
        item.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                perform();
            }
        });

        // Defines the default policy.
        inactivationPolicy = InactivationPolicy.DISABLE_POLICY;
    }

    /**
     * Sets the inactivation policy.
     * 
     * @param inactivationPolicy
     *            The new inactivation policy.
     */
    public void setInactivationPolicy(InactivationPolicy inactivationPolicy) {

        if (inactivationPolicy == null) {
            return;
        }

        this.inactivationPolicy = inactivationPolicy;
    }

    /**
     * Inactive this action considering the inactivation policy.
     * 
     * @param inactivationMessage
     *            The inactivation message used as a tool tip to inform the
     *            user.
     */
    public void inactive(String inactivationMessage) {

        switch (inactivationPolicy) {
        case DISABLE_POLICY:
            item.setEnabled(false);
            break;
        case HIDE_POLICY:
            item.setVisible(false);
            break;
        }

        item.setTitle(inactivationMessage);
    }

    /**
     * Active this action.
     */
    public void active() {

        switch (inactivationPolicy) {
        case DISABLE_POLICY:
            item.setEnabled(true);
            break;
        case HIDE_POLICY:
            item.setVisible(true);
            break;
        }

        item.setTitle(null);
    }

    /**
     * Returns the menu item which symbolizes this menu action.
     * 
     * @return The menu item of this action.
     */
    public MenuItem getMenuItem() {
        return item;
    }

    /**
     * Gets the action's label.
     * 
     * @return The action's label.
     */
    public abstract String getText();

    /**
     * Gets the action's icon.
     * 
     * @return The action's icon.
     */
    public abstract AbstractImagePrototype getIcon();

    /**
     * Returns if this action can be now performed. A <code>null</code> or empty
     * string means that the action can be perform. A non-empty string means
     * that the action cannot be performed for the moment.<br/>
     * The returned string will be used as the cause of this unavailability.
     * 
     * @return The unavailability message, or <code>null</code>.
     */
    public abstract String canBePerformed();

    /**
     * Performs this action.
     */
    public abstract void perform();
}
