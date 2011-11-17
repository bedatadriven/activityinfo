package org.sigmah.client.page.entry.form;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextArea;

public class CommentSection extends FormSectionWithFormLayout<SiteDTO> {

	private TextArea commentField;

	public CommentSection() {
		
		getFormLayout().setLabelAlign(LabelAlign.TOP);

		commentField = new TextArea();
        commentField.setName("comments");
        commentField.setFieldLabel(I18N.CONSTANTS.comments());
        commentField.setHeight(300);
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
