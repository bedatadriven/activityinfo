package org.activityinfo.client.page.entry.form.field;

import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class AttributeCombo extends ComboBox<AttributeDTO> implements AttributeField {


	public AttributeCombo(AttributeGroupDTO attributeGroup) {
		super();
		this.setFieldLabel(Format.htmlEncode(attributeGroup.getName()));
		this.setDisplayField("name");
		this.setTriggerAction(TriggerAction.ALL);
		this.setEditable(false);
		
		ListStore<AttributeDTO> store = new ListStore<AttributeDTO>();
		store.add(attributeGroup.getAttributes());
		
		setStore(store);	
	}

	@Override
	public void updateForm(SiteDTO site) {
		for(AttributeDTO attribute : getStore().getModels()) {
			if(site.getAttributeValue(attribute.getId())) {
				setValue(attribute);
				return;
			}
		}
		setValue(null);
	}

	@Override
	public void updateModel(SiteDTO site) {
		AttributeDTO selected = getValue();
		
		for(AttributeDTO attribute : getStore().getModels()) {
			site.setAttributeValue(attribute.getId(), 
					selected != null && selected.getId() == attribute.getId());
		}
	}
}
