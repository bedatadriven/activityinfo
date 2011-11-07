package org.sigmah.client.page.entry.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.LabelField;

public class SelectedLocationFieldSet extends AbstractFieldSet {
	private FormPanel form = new FormPanel();
	private LabelField labelName = new LabelField();
	private LabelField labelAxe = new LabelField();
	private LabelField labelType = new LabelField();
	private List<LabelField> adminEntityLabels = new ArrayList<LabelField>();
	
	public SelectedLocationFieldSet(ActivityDTO activity) {
		super(activity.getName(), 80, 160);
		
		form.setLabelAlign(LabelAlign.LEFT);
		form.add(labelName);
		form.add(labelAxe);
		form.add(labelType);
		labelType.setText(activity.getLocationType().getName());
	}
	
	public void setLocation(LocationDTO location) {
		labelName.setText(location.getName());
		labelAxe.setText(location.getAxe());
	}
	
	public void setAdminEntities(Map<AdminLevelDTO, AdminEntityDTO> adminEntitiesByLevel) {
		for (LabelField labelField : adminEntityLabels) {
			remove(labelField);
		}
		
		for (Entry<AdminLevelDTO, AdminEntityDTO> adminLevel : adminEntitiesByLevel.entrySet()) {
			LabelField labelEntity = new LabelField();
			labelEntity.setText(adminLevel.getValue().getName());
			labelEntity.setFieldLabel(adminLevel.getKey().getName());
			add(labelEntity);
			adminEntityLabels.add(labelEntity);
		}
	}
}
