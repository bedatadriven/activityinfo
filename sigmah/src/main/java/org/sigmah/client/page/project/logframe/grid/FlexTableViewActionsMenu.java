package org.sigmah.client.page.project.logframe.grid;

import java.util.ArrayList;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.project.logframe.grid.MenuAction.InactivationPolicy;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

/**
 * Represents an actions menu for a row in the log frame grid.
 * 
 * @author tmi
 * 
 */
public abstract class FlexTableViewActionsMenu {

    /**
     * The view.
     */
    private final FlexTableView view;

    /**
     * The row.
     */
    private final Row<?> row;

    /**
     * The menu.
     */
    private final Menu menu;

    /**
     * The inactivation policy.
     */
    private InactivationPolicy inactivationPolicy;

    /**
     * All menu actions.
     */
    private final ArrayList<MenuAction> actions = new ArrayList<MenuAction>();

    /**
     * Builds this menu.
     * 
     * @param view
     *            The view where this menu is displayed.
     * @param row
     *            The row managed by this menu.
     */
    public FlexTableViewActionsMenu(final FlexTableView view, final Row<?> row) {

        // Sets local attributes.
        this.view = view;
        this.row = row;

        // Defines the default policy.
        inactivationPolicy = InactivationPolicy.DISABLE_POLICY;

        // Move up action.
        final MenuItem upMenuItem = createMoveUpAction();

        // Move down action.
        final MenuItem downMenuItem = createMoveDownAction();

        // Delete action.
        final MenuItem deleteMenuItem = createDeleteAction();

        // Menu.
        menu = new Menu();
        menu.add(upMenuItem);
        menu.add(downMenuItem);
        menu.add(deleteMenuItem);

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
     * Builds and returns the move up action.
     * 
     * @return The move up action.
     */
    private MenuItem createMoveUpAction() {

        final MenuAction action = new MenuAction() {

            private final int movesCount = 1;

            @Override
            public void perform() {

                // Tries to move up the element.
                if (beforeMoveDown()) {

                    view.moveRow(row.getParent(), row.getId(), movesCount);
                }
            }

            @Override
            public String getText() {
                return I18N.CONSTANTS.logFrameActionUp();
            }

            @Override
            public AbstractImagePrototype getIcon() {
                return IconImageBundle.ICONS.up();
            }

            @Override
            public String canBePerformed() {

                final boolean canBeMoved = canBeMovedUp() && view.canBeMoved(row.getParent(), row.getId(), movesCount);

                if (canBeMoved) {
                    return null;
                } else {
                    return I18N.CONSTANTS.logFrameActionUpUnavailable();
                }
            }
        };

        action.setInactivationPolicy(inactivationPolicy);

        // Adds it locally.
        actions.add(action);

        return action.getMenuItem();
    }

    /**
     * Builds and returns the move down action.
     * 
     * @return The move down action.
     */
    private MenuItem createMoveDownAction() {

        final MenuAction action = new MenuAction() {

            private final int movesCount = -1;

            @Override
            public void perform() {

                // Tries to move down the element.
                if (beforeMoveDown()) {

                    view.moveRow(row.getParent(), row.getId(), movesCount);
                }
            }

            @Override
            public String getText() {
                return I18N.CONSTANTS.logFrameActionDown();
            }

            @Override
            public AbstractImagePrototype getIcon() {
                return IconImageBundle.ICONS.down();
            }

            @Override
            public String canBePerformed() {

                final boolean canBeMoved = canBeMovedDown()
                        && view.canBeMoved(row.getParent(), row.getId(), movesCount);

                if (canBeMoved) {
                    return null;
                } else {
                    return I18N.CONSTANTS.logFrameActionDownUnavailable();
                }
            }
        };

        action.setInactivationPolicy(inactivationPolicy);

        // Adds it locally.
        actions.add(action);

        return action.getMenuItem();
    }

    /**
     * Builds and returns the delete action.
     * 
     * @return The delete action.
     */
    private MenuItem createDeleteAction() {

        final MenuAction action = new MenuAction() {

            @Override
            public void perform() {

                // Tries to remove the element.
                if (beforeRemove()) {

                    // Removes the row from the view.
                    view.removeRow(row.getParent(), row.getId());
                }
            }

            @Override
            public String getText() {
                return I18N.CONSTANTS.logFrameActionDelete();
            }

            @Override
            public AbstractImagePrototype getIcon() {
                return IconImageBundle.ICONS.delete();
            }

            @Override
            public String canBePerformed() {

                final boolean canBeRemoved = canBeRemoved();

                if (canBeRemoved) {
                    return null;
                } else {
                    return I18N.CONSTANTS.logFrameActionDeleteUnavailable();
                }
            }
        };

        action.setInactivationPolicy(inactivationPolicy);

        // Adds it locally.
        actions.add(action);

        return action.getMenuItem();
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
     * Returns if the element managed by this menu can be removed.
     * 
     * @return If the element can be removed.
     */
    public abstract boolean canBeRemoved();

    /**
     * Method called just before removing the element managed by this menu. If
     * this method returns <code>true</code>, the corresponding row will be
     * removed in the view. Otherwise, this method has no effect.
     * 
     * @return If the element has been removed.
     */
    public abstract boolean beforeRemove();

    /**
     * Returns if the element managed by this menu can be moved up.
     * 
     * @return If the element can be moved up.
     */
    public abstract boolean canBeMovedUp();

    /**
     * Method called just before moving up the element managed by this menu. If
     * this method returns <code>true</code>, the corresponding row will be
     * moved up in the view. Otherwise, this method has no effect.
     * 
     * @return If the element has been moved up.
     */
    public abstract boolean beforeMoveUp();

    /**
     * Returns if the element managed by this menu can be moved down.
     * 
     * @return If the element can be moved down.
     */
    public abstract boolean canBeMovedDown();

    /**
     * Method called just before moving down the element managed by this menu.
     * If this method returns <code>true</code>, the corresponding row will be
     * moved down in the view. Otherwise, this method has no effect.
     * 
     * @return If the element has been moved up.
     */
    public abstract boolean beforeMoveDown();
}
