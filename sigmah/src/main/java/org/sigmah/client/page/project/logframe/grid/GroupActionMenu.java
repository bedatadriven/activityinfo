package org.sigmah.client.page.project.logframe.grid;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Represents an actions menu for a group in the log frame grid.
 * 
 * @author tmi
 * 
 */
public abstract class GroupActionMenu extends ActionsMenu {

    /**
     * The row.
     */
    private final RowsGroup<?> group;

    /**
     * Builds this menu.
     * 
     * @param view
     *            The view where this menu is displayed.
     * @param group
     *            The group managed by this menu.
     */
    public GroupActionMenu(FlexTableView view, RowsGroup<?> group) {
        super(view);

        this.group = group;

        // Rename action.
        final MenuItem renameMenuItem = createRenameAction();

        // Menu.
        menu.add(renameMenuItem);
    }

    /**
     * Builds and returns the rename action.
     * 
     * @return The rename action.
     */
    private MenuItem createRenameAction() {

        final MenuAction action = new MenuAction() {

            @Override
            public void perform() {

                // Tries to rename the element.
                beforeRename(new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        // nothing.
                    }

                    @Override
                    public void onSuccess(String result) {
                        view.refreshGroupWidget(group);
                    }
                });
            }

            @Override
            public String getText() {
                return I18N.CONSTANTS.logFrameActionRename();
            }

            @Override
            public AbstractImagePrototype getIcon() {
                return IconImageBundle.ICONS.rename();
            }

            @Override
            public String canBePerformed() {

                final boolean canBeRenamed = canBeRemaned();

                if (canBeRenamed) {
                    return null;
                } else {
                    return I18N.CONSTANTS.logFrameActionRenameUnavailable();
                }
            }
        };

        action.setInactivationPolicy(inactivationPolicy);

        // Adds it locally.
        actions.add(action);

        return action.getMenuItem();
    }

    /**
     * Returns if the element managed by this menu can be renamed.
     * 
     * @return If the element can be renamed.
     */
    public abstract boolean canBeRemaned();

    /**
     * Method called just before renaming the element managed by this menu. If
     * this method returns <code>true</code>, the corresponding group will be
     * renamed in the view. Otherwise, this method has no effect.
     * 
     * @param callback
     *            Called after the group has been renamed.
     */
    public abstract void beforeRename(AsyncCallback<String> callback);

}
