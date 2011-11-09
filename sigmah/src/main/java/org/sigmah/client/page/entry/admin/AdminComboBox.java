package org.sigmah.client.page.entry.admin;

import java.awt.Event;

import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.El.VisMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class AdminComboBox extends ComboBox<AdminEntityDTO> {

	private El clearSpan;
	private final AdminLevelDTO level;
	
	public AdminComboBox(AdminLevelDTO level, ListStore<AdminEntityDTO> store) {
		this.level = level;
		setFieldLabel(level.getName());
		setStore(store);
		setTypeAhead(false);
		setForceSelection(true);
		setEditable(false);
		setValueField("id");
		setUseQueryCache(false);
		setDisplayField("name");
		setTriggerAction(TriggerAction.ALL);
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		clearSpan = new El(DOM.createSpan());
		clearSpan.setInnerHtml("clear");
		clearSpan.addStyleName(SiteFormResources.INSTANCE.style().adminClearSpan());
		clearSpan.addEventsSunk(Event.MOUSE_DOWN);
		clearSpan.setVisibilityMode(VisMode.VISIBILITY);
		clearSpan.setVisible(false);
		
		getElement().appendChild(clearSpan.dom);
	}
	
	public AdminLevelDTO getLevel() {
		return level;
	}

	@Override
	public void setValue(AdminEntityDTO value) {
		super.setValue(value);
		
		this.clearSpan.setVisible(this.value != null);
	}
	
	@Override
	protected void onClick(ComponentEvent ce) {
		if (clearSpan.dom.isOrHasChild(ce.getTarget())){
			setValue(null);
		}
		super.onClick(ce);
	}
}
