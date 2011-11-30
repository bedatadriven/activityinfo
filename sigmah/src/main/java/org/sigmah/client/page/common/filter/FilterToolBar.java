package org.sigmah.client.page.common.filter;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public final class FilterToolBar extends ToolBar {

	private Button applyButton;
	private Button removeButton;
	
	public interface ApplyFilterHandler extends EventHandler {
		void onApplyFilter(ApplyFilterEvent deleteEvent);
	}

	public interface RemoveFilterHandler extends EventHandler {
		void onRemoveFilter(RemoveFilterEvent deleteEvent);
	}
	
	public static class ApplyFilterEvent extends GwtEvent<ApplyFilterHandler> {
		public static final Type TYPE = new Type<ApplyFilterHandler>(); 

		@Override
		public Type<ApplyFilterHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(ApplyFilterHandler handler) {
			handler.onApplyFilter(this);
		}
	}
	
	public static class RemoveFilterEvent extends GwtEvent<RemoveFilterHandler> {
		public static final sType TYPE = new Type<RemoveFilterHandler>(); 

		@Override
		public Type<RemoveFilterHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RemoveFilterHandler handler) {
			handler.onRemoveFilter(this);
		}
	}
	
	public FilterToolBar() {
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

	public HandlerRegistration addApplyFilterHandler(ApplyFilterHandler handler) {
		return this.addHandler(handler, ApplyFilterEvent.TYPE);
	}

	public HandlerRegistration addRemoveFilterHandler(
			RemoveFilterHandler handler) {
		return this.addHandler(handler, RemoveFilterEvent.TYPE);
	}

	public void setRemoveFilterEnabled(boolean enabled) {
		removeButton.setEnabled(enabled);
	}

	public void setApplyFilterEnabled(boolean enabled) {
		applyButton.setEnabled(enabled);
	}
	
}
