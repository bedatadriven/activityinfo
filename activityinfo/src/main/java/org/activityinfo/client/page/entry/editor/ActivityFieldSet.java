package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.Application;
import org.activityinfo.client.page.entry.editor.AbstractFieldSet;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.SiteModel;

import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;

import java.util.Date;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class
        ActivityFieldSet extends AbstractFieldSet {
    private DateField dateField1;
    private DateField dateField2;

    public ActivityFieldSet(ActivityModel activity,
                            ListStore<PartnerModel> partnerStore,
                            ListStore<SiteModel> assessmentStore) {
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


		ComboBox<PartnerModel> partnerCombo = new ComboBox<PartnerModel>();
		partnerCombo.setName("partner");
		partnerCombo.setDisplayField("name");
		partnerCombo.setEditable(false);
		partnerCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
		partnerCombo.setStore(partnerStore);
		partnerCombo.setFieldLabel(Application.CONSTANTS.partner());
		partnerCombo.setForceSelection(true);
		partnerCombo.setAllowBlank(false);
		add(partnerCombo);

		if(activity.getReportingFrequency() == ActivityModel.REPORT_ONCE) {
			
//			ListStore<Status> statusStore = new ListStore<Status>();
//			statusStore.add(Status.getStatusValues());
//
//			final MappingComboBox statusCombo = new MappingComboBox();
//			statusCombo.setName("status");
//			statusCombo.setFieldLabel(Application.CONSTANTS.status());
//			statusCombo.setTriggerAction(ComboBox.TriggerAction.ALL);
//			statusCombo.setAllowBlank(false);
//			statusCombo.setEditable(false);
//			statusCombo.add(-2, Application.CONSTANTS.planned());
//			statusCombo.add(-1, Application.CONSTANTS.inProgress());
//			statusCombo.add(0, Application.CONSTANTS.cancelled());
//			statusCombo.add(1, Application.CONSTANTS.complete());
//			add(statusCombo);

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
//			ComboBox<SiteModel> assessmentCombo = new AssessmentCombo(activity.getDatabase().getCountry());
//			assessmentCombo.setName("assessment");
//			assessmentCombo.setFieldLabel(Application.CONSTANTS.assessment());
//			add(assessmentCombo);
//		}
    }

}
