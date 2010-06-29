/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.*;
import org.sigmah.client.Application;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class
        ActivityFieldSet extends AbstractFieldSet {
    private DateField dateField1;
    private DateField dateField2;

    public ActivityFieldSet(ActivityDTO activity,
                            ListStore<PartnerDTO> partnerStore,
                            ListStore<SiteDTO> assessmentStore) {
        super(Application.CONSTANTS.activity(), 100, 200);

        TextField<String> databaseField = new TextField<String>();
		databaseField.setValue(activity.getDatabase().getName());
		databaseField.setFieldLabel(Application.CONSTANTS.database());
		databaseField.setReadOnly(true);
		add(databaseField);

		TextField<String> activityField = new TextField<String>();
		activityField.setValue(activity.getName());
		activityField.setFieldLabel(Application.CONSTANTS.activity());
		activityField.setReadOnly(true);
		add(activityField);


		ComboBox<PartnerDTO> partnerCombo = new ComboBox<PartnerDTO>();
		partnerCombo.setName("partner");
		partnerCombo.setDisplayField("name");
		partnerCombo.setEditable(false);
		partnerCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
		partnerCombo.setStore(partnerStore);
		partnerCombo.setFieldLabel(Application.CONSTANTS.partner());
		partnerCombo.setForceSelection(true);
		partnerCombo.setAllowBlank(false);
		add(partnerCombo);

		if(activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
			
            dateField1 = new DateField();
			dateField1.setName("date1");
			dateField1.setAllowBlank(false);
			dateField1.setFieldLabel(Application.CONSTANTS.startDate());
			add(dateField1);

            dateField2 = new DateField();
			dateField2.setName("date2");
			dateField2.setAllowBlank(false);
			dateField2.setFieldLabel(Application.CONSTANTS.endDate());
            dateField2.setValidator(new Validator() {
                public String validate(Field<?> field, String value) {
                    if(dateField1.getValue()!=null && dateField2.getValue()!=null) {
                        if(dateField2.getValue().before(dateField1.getValue())) {
                            return Application.CONSTANTS.inconsistentDateRangeWarning();
                        }
                    }
                    return null;
                }
            });
			add(dateField2);

		}

//		if(activity.getLocationType().getBoundAdminLevelId() == null) {
//			ComboBox<SiteDTO> assessmentCombo = new AssessmentCombo(activity.getDatabase().getCountry());
//			assessmentCombo.setName("assessment");
//			assessmentCombo.setFieldLabel(Application.CONSTANTS.assessment());
//			add(assessmentCombo);
//		}
    }

}
