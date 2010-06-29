/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import com.extjs.gxt.ui.client.widget.form.TextField;
import org.sigmah.client.Application;
import org.sigmah.shared.dto.ActivityDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class LocationFieldSet extends AdminFieldSet {


    public LocationFieldSet(ActivityDTO activity) {
        super(activity);

        if (activity.getLocationType().getBoundAdminLevelId() == null) {

            TextField<String> nameField = new TextField<String>();
            nameField.setName("locationName");
            nameField.setFieldLabel(activity.getLocationType().getName());
            nameField.setAllowBlank(false);
            add(nameField);

            TextField<String> axeField = new TextField<String>();
            axeField.setName("locationAxe");
            axeField.setFieldLabel(Application.CONSTANTS.axe());
            add(axeField);
        }
    }

}
