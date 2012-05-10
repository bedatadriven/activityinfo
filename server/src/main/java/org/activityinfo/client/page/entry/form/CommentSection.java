package org.activityinfo.client.page.entry.form;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextArea;

public class CommentSection extends FormSectionWithFormLayout<SiteDTO> {

	private TextArea commentField;

	public CommentSection() {
		
		getFormLayout().setLabelAlign(LabelAlign.TOP);

		commentField = new TextArea();
        commentField.setName("comments");
        commentField.setFieldLabel(I18N.CONSTANTS.comments());
        commentField.setWidth(350);
		add(commentField);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void updateModel(SiteDTO m) {
		m.setComments(commentField.getValue());
	}

	@Override
	public void updateForm(SiteDTO m) {
		commentField.setValue(m.getComments());
	}
	
}
