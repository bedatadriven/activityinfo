/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.common.toolbar;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Convenience subclass for the GXT toolbar that directs all tool actions
 * through a common choke point implementing
 * {@link org.activityinfo.client.page.common.toolbar.ActionListener}
 * 
 * Also centralizes look&feel of common buttons like New, Edit, Refresh, Save,
 * etc.
 * 
 */
public class ActionToolBar extends ToolBar implements Listener<ButtonEvent> {

	private ActionListener listener;
	private SplitButton saveButton;
	private Button addButton;
	private Button removeButton;
	private Button editButton;
	private Button uploadButton;
	private Button printButton;

	public ActionToolBar() {
	}

	public ActionToolBar(ActionListener listener) {
		this.listener = listener;
		setEnabled(listener != null);
	}

	/**
	 * 
	 * @param actionId
	 *            The id to be provided to the
	 *            {@link org.activityinfo.client.page.common.toolbar.ActionListener}
	 *            if the button is selected
	 * @param text
	 *            Text of the button
	 * @param icon
	 *            Icon of the button. See
	 *            {@link org.activityinfo.client.icon.IconImageBundle}
	 */
	public Button addButton(String actionId, String text,
			AbstractImagePrototype icon) {
		Button button = new Button(text, icon);
		button.setItemId(actionId);
		button.addListener(Events.Select, this);
		add(button);

		return button;
	}
	

	public void add(Iterable<? extends Component> buttons) {
		for(Component component : buttons) {
			add(component);
		}
	}

	public ToggleButton addToggleButton(String actionId, String text,
			AbstractImagePrototype icon) {
		ToggleButton button = new ToggleButton(text, icon);
		button.setItemId(actionId);
		button.addListener(Events.Select, this);
		add(button);

		return button;
	}
	
    public ToggleButtonBuilder addToggleButton() {
    	return new ToggleButtonBuilder();
    }
    

	public void addCreateButton() {
		this.addButton = addButton(UIActions.ADD, I18N.CONSTANTS.addItem(),
				IconImageBundle.ICONS.add());

	}

	public void addPrintButton() {
		addButton(UIActions.PRINT,
				I18N.CONSTANTS.printForm(), IconImageBundle.ICONS.printer());
	}

	public void addUploadButton() {
		addButton(UIActions.UPLOAD,
				I18N.CONSTANTS.upload(), IconImageBundle.ICONS.up());
		
	}

	public void addEditButton() {
		addEditButton(IconImageBundle.ICONS.editPage());
	}

	public void addEditButton(AbstractImagePrototype icon) {
		this.editButton = addButton(UIActions.EDIT, I18N.CONSTANTS.edit(), icon);
	}

	public void addDeleteButton() {
		this.removeButton = addButton(UIActions.DELETE,
				I18N.CONSTANTS.delete(), IconImageBundle.ICONS.delete());
	}

	public void addDeleteButton(String text) {
		this.removeButton = addButton(UIActions.DELETE, text,
				IconImageBundle.ICONS.delete());
	}

	public void addExcelExportButton() {
		addButton(UIActions.EXPORT, I18N.CONSTANTS.export(),
				IconImageBundle.ICONS.excel());
	}

	public void addRefreshButton() {
		addButton(UIActions.REFRESH,
				I18N.CONSTANTS.refreshPreview(),
				IconImageBundle.ICONS.refresh());
	}


	public void addSaveSplitButton() {
		saveButton = new SplitButton(I18N.CONSTANTS.save());
		saveButton.setIcon(IconImageBundle.ICONS.save());
		saveButton.setItemId(UIActions.SAVE);
		saveButton.addListener(Events.Select, this);

		Menu menu = new Menu();
		MenuItem saveItem = new MenuItem(I18N.CONSTANTS.save(),
				IconImageBundle.ICONS.save(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						if (listener != null) {
							listener.onUIAction(UIActions.SAVE);
						}
					}
				});
		menu.add(saveItem);

		MenuItem discardItem = new MenuItem(I18N.CONSTANTS.discardChanges(),
				IconImageBundle.ICONS.cancel(),
				new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						listener.onUIAction(UIActions.DISCARD_CHANGES);
					}
				});
		menu.add(discardItem);

		saveButton.setMenu(menu);

		add(saveButton);
	}

	public void setDirty(boolean dirty) {
		if (saveButton != null) {
			saveButton.setEnabled(dirty);
			if (dirty) {
				saveButton.setText(I18N.CONSTANTS.save());
				saveButton.setIcon(IconImageBundle.ICONS.save());
			} else {
				saveButton.setText(I18N.CONSTANTS.saved());
			}
		}
	}

	@Override
	public void handleEvent(ButtonEvent be) {
		if (listener != null) {
			listener.onUIAction(be.getButton().getItemId());
		}
	}

	public void setPrintEnabled(boolean enabled) {
		if (printButton != null) {
			printButton.setEnabled(enabled);
		}
	}

	public void setUploadEnabled(boolean enabled) {
		if (uploadButton != null) {
			uploadButton.setEnabled(enabled);
		}
	}

	public void setActionEnabled(String actionId, boolean enabled) {
		Component c = getItemByItemId(actionId);

		if (c != null) {
			c.setEnabled(enabled);
		} else {
			Log.warn("ActionToolBar: setActionEnabled(" + actionId
					+ ") was called, but button is not present");
		}
	}

	public void setDeleteEnabled(boolean enabled) {
		if (removeButton != null) {
			removeButton.setEnabled(enabled);
		}
	}

	public void setAddEnabled(boolean enabled) {
		if (addButton != null) {
			addButton.setEnabled(enabled);
		}
	}

	public void setUpdateEnabled(boolean enabled) {
		if (editButton != null) {
			editButton.setEnabled(enabled);
		}
	}

	public void setListener(ActionListener listener) {
		this.listener = listener;
		setEnabled(listener != null);
	}

	public void addLockedPeriodsButton() {
		addButton(UIActions.SHOW_LOCKED_PERIODS, I18N.CONSTANTS.timeLocks(),
				IconImageBundle.ICONS.lockedPeriod());
	}

	public class ToggleButtonBuilder {
		
		private ToggleButton button;
		
		public ToggleButtonBuilder() {
			button = new ToggleButton();
			add(button);
		}
		
		public ToggleButtonBuilder withText(String text) {
			button.setText(text);
			return this;
		}
		
		public ToggleButtonBuilder withIcon(AbstractImagePrototype icon) {
			button.setIcon(icon);
			return this;
		}
		
		public ToggleButtonBuilder withListener(SelectionListener<ButtonEvent> listener) {
			button.addSelectionListener(listener);
			return this;
		}
		
		public ToggleButtonBuilder inGroup(String group) {
			button.setToggleGroup(group);
			return this;
		}
	}

	
}
