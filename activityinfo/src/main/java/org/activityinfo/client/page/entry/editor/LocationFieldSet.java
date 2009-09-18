package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.page.entry.editor.AdminFieldSet;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.AdminLevelModel;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.store.ListStore;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class LocationFieldSet extends AdminFieldSet {


    public LocationFieldSet(ActivityModel activity) {
        super(activity);

    	if(activity.getLocationType().getBoundAdminLevelId() == null) {

			TextField<String> nameField = new TextField<String>();
			nameField.setName("locationName");
			nameField.setFieldLabel(activity.getLocationType().getName());
			nameField.setAllowBlank(false);
            add(nameField);

			TextField<String> axeField = new TextField<String>();
			axeField.setName("locationAxe");
			axeField.setFieldLabel("Axe");
			add(axeField);
        }
    }

}
