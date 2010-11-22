package org.sigmah.client.page.project.logframe.grid;

import java.util.ArrayList;

import org.sigmah.client.page.project.logframe.grid.MenuAction.InactivationPolicy;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.user.client.ui.Widget;

/**
 * Represents an actions menu for the log frame grid.
 * 
 * @author tmi
 * 
 */
public abstract class ActionsMenu {

    /**
     * The view.
     */
    protected final FlexTableView view;

    /**
     * The menu.
     */
    protected final Menu menu;

    /**
     * The inactivation policy.
     */
    protected InactivationPolicy inactivationPolicy;

    /**
     * All menu actions.
     */
    protected final ArrayList<MenuAction> actions;

    public ActionsMenu(final FlexTableView view) {

        this.view = view;
        actions = new ArrayList<MenuAction>();

        // Menu.
        menu = new Menu();

        // Adds a listener to update the menu each time it is shown.
        menu.addListener(Events.BeforeShow, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                for (final MenuAction action : actions) {

                    // Checks if this action can be performed in the current
                    // state.
                    final String msg = action.canBePerformed();

                    // The action can be performed.
                    if (msg == null || "".equals(msg.trim())) {
                        action.active();
                    }
                    // The action cannot be performed.
                    else {
                        action.inactive(msg);
                    }
                }
            }
        });
    }

    /**
     * Displays this menu relative to the widget using the default alignment.
     * 
     * @param widget
     *            the align widget
     */
    public void show(Widget widget) {
        menu.show(widget);
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

        // Updates each item policy.
        for (final MenuAction action : actions) {
            action.setInactivationPolicy(inactivationPolicy);
        }
    }

}
