package org.sigmah.client.page.entry.form.field;

import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class AttributeCombo extends ComboBox<AttributeDTO> implements AttributeField {


	public AttributeCombo(AttributeGroupDTO attributeGroup) {
		super();
		
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
