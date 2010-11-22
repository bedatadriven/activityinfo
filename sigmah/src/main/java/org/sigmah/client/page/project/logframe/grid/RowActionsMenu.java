package org.sigmah.client.page.project.logframe.grid;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Represents an actions menu for a row in the log frame grid.
 * 
 * @author tmi
 * 
 */
public abstract class RowActionsMenu extends ActionsMenu {

    /**
     * The row.
     */
    private final Row<?> row;

    /**
     * Builds this menu.
     * 
     * @param view
     *            The view where this menu is displayed.
     * @param row
     *            The row managed by this menu.
     */
    public RowActionsMenu(final FlexTableView view, final Row<?> row) {

        super(view);

        // Sets local attributes.
        this.row = row;

        // Move up action.
        final MenuItem upMenuItem = createMoveUpAction();

        // Move down action.
        final MenuItem downMenuItem = createMoveDownAction();

        // Delete action.
        final MenuItem deleteMenuItem = createDeleteAction();

        // Menu.
        menu.add(upMenuItem);
        menu.add(downMenuItem);
        menu.add(deleteMenuItem);
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
