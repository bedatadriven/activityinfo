package org.sigmah.client.page.entry.location;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;
import org.sigmah.shared.dto.ActivityDTO;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

/**
 * Presents a form dialog for a Site for an Activity that 
 * has a LocationType that is bound to an AdminLevel
 */
public class SiteWithBoundLocationTypeForm extends Dialog {

	private TextArea commentField;
	private AdminFieldSetPresenter adminFieldSet;

	public SiteWithBoundLocationTypeForm(Dispatcher dispatcher, ActivityDTO activity) {
		FormLayout layout = new FormLayout();
		setLayout(layout);
		setWidth(400);
		setHeight(250);

		setHeading(I18N.MESSAGES.addNewSiteForActivity(activity.getName()));
		adminFieldSet = new AdminFieldSetPresenter(dispatcher, activity
				.getDatabase().getCountry(), activity.getAdminLevels());

		AdminComboBoxSet comboBoxes = new AdminComboBoxSet(adminFieldSet);

		for (AdminComboBox comboBox : comboBoxes) {
			add(comboBox);
		}
		
		commentField = new TextArea();
		commentField.setName("comments");
		commentField.setFieldLabel(I18N.CONSTANTS.comments());
		commentField.setWidth(350);
		commentField.setHeight(200);
		add(commentField);
		layout();
	}

}
