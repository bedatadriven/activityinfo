package org.sigmah.client.page.common.filter;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.shared.HandlerRegistration;

public class FilterToolBarImpl extends ToolBar implements FilterToolBar {

	private Button applyButton;
	private Button removeButton;
	
	public FilterToolBarImpl() {
		super();

		initializeComponent();
		
		createApplyButton();
		createRemoveButton();
	}

	private void initializeComponent() {
    }

	private void createRemoveButton() {
    	removeButton = new Button(
    			I18N.CONSTANTS.remove(), 
    			IconImageBundle.ICONS.delete(), 
    	
		    	new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						fireEvent(new RemoveFilterEvent());
					}
				});
    	
		add(removeButton);
		setRemoveFilterEnabled(false);
	}

	private void createApplyButton() {
    	applyButton = new Button(
    			I18N.CONSTANTS.apply(), 
    			IconImageBundle.ICONS.applyFilter(), 
    	
		    	new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						fireEvent(new ApplyFilterEvent());
					}
				});
    	
		add(applyButton);
		setApplyFilterEnabled(false);
	}

	@Override
	public HandlerRegistration addApplyFilterHandler(ApplyFilterHandler handler) {
		return this.addHandler(handler, ApplyFilterEvent.TYPE);
	}

	@Override
	public HandlerRegistration addRemoveFilterHandler(
			RemoveFilterHandler handler) {
		return this.addHandler(handler, RemoveFilterEvent.TYPE);
	}

	@Override
	public void setRemoveFilterEnabled(boolean enabled) {
		removeButton.setEnabled(enabled);
	}

	@Override
	public void setApplyFilterEnabled(boolean enabled) {
		applyButton.setEnabled(enabled);
	}
	
}
