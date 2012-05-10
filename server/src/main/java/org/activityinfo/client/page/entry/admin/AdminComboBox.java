package org.activityinfo.client.page.entry.admin;

import org.activityinfo.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.google.gwt.user.client.ui.Widget;

public interface AdminComboBox {

	Widget asWidget();
	void addSelectionChangeListener(Listener<SelectionChangedEvent> listener);
	void setValue(AdminEntityDTO value);
	void setEnabled(boolean enabled);
	boolean validate();

}
