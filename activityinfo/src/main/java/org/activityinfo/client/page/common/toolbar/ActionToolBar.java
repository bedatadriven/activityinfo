package org.activityinfo.client.page.common.toolbar;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import org.activityinfo.client.Application;

/**
 * Convienence subclass for the GXT toolbar that directs all tool actions
 * through a common choke point implementing {@link org.activityinfo.client.page.common.toolbar.ActionListener}
 *
 * Also centralizes look&feel of common buttons like New, Edit, Refresh, Save, etc.
 *
 */
public class ActionToolBar extends ToolBar implements Listener<ButtonEvent> {

    private ActionListener listener;
    private SplitButton saveButton;

    public ActionToolBar() {
    }

    public ActionToolBar(ActionListener listener) {
        this.listener = listener;
        setEnabled(listener != null);
    }

    /**
     *
     * @param actionId The id to be provided to the {@link org.activityinfo.client.page.common.toolbar.ActionListener} if
     * the button is selected
     * @param text Text of the button
     * @param icon Icon of the button. See {@link org.activityinfo.client.icon.IconImageBundle}
     */
    public void addButton(String actionId, String text, AbstractImagePrototype icon) {
        Button button = new Button(text, icon);
        button.setItemId(actionId);
        button.addListener(Events.Select, this);
        add(button);
    }

    public void addEditButton() {
        addEditButton(Application.ICONS.editPage());
    }

    public void addEditButton(AbstractImagePrototype icon) {
      addButton(UIActions.edit, Application.CONSTANTS.edit(), icon);
    }

    public void addDeleteButton() {
        addButton(UIActions.delete, Application.CONSTANTS.delete(), Application.ICONS.delete());
    }

    public void addDeleteButton(String text) {
        addButton(UIActions.delete, text, Application.ICONS.delete());
    }

    public void addExcelExportButton() {
        addButton(UIActions.export, Application.CONSTANTS.export(), Application.ICONS.excel());
    }

    public void addRefreshButton() {
        addButton(UIActions.refresh, Application.CONSTANTS.refreshPreview(), Application.ICONS.refresh());
    }

    public void addSaveSplitButton() {
        saveButton = new SplitButton(Application.CONSTANTS.save());
        saveButton.setIcon(Application.ICONS.save());
        saveButton.setItemId(UIActions.save);
        saveButton.addListener(Events.Select, this);

        Menu menu = new Menu();
        MenuItem saveItem = new MenuItem(Application.CONSTANTS.save(), Application.ICONS.save(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                if(listener!=null) {
                    listener.onUIAction(UIActions.save);
                }
            }
        });
        menu.add(saveItem);

        MenuItem discardItem = new MenuItem(Application.CONSTANTS.discardChanges(), Application.ICONS.cancel(),
            new SelectionListener<MenuEvent>() {
                @Override
                public void componentSelected(MenuEvent ce) {
                    listener.onUIAction(UIActions.discardChanges);
                }
            });
        menu.add(discardItem);

        saveButton.setMenu(menu);

        add(saveButton);
    }

    public void setDirty(boolean dirty) {
        if(saveButton != null) {
            saveButton.setEnabled(dirty);
            if(dirty) {
                saveButton.setText(Application.CONSTANTS.save());
                saveButton.setIcon(Application.ICONS.save());
            } else{
                saveButton.setText(Application.CONSTANTS.saved());
            }
        }
    }


    public void handleEvent(ButtonEvent be) {
        if(listener != null) {
            listener.onUIAction(be.getButton().getItemId());
        }
    }

    public void setActionEnabled(String actionId, boolean enabled) {
        Component c = getItemByItemId(actionId);

        if(c!=null) {
            c.setEnabled(enabled);
        } else {
            GWT.log("ActionToolBar: setActionEnabled(" + actionId + ") was called, but button is not present", null);
        }
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
        setEnabled(listener != null);
    }


}
