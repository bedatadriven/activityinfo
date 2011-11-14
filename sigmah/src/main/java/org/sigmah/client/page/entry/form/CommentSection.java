package org.sigmah.client.page.entry.form;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextArea;

public class CommentSection extends FormSection {

	public CommentSection() {
		
		getFormLayout().setLabelAlign(LabelAlign.TOP);

		TextArea commentField = new TextArea();
        commentField.setName("comments");
        commentField.setFieldLabel(I18N.CONSTANTS.comments());
        commentField.setHeight(300);
		add(commentField);
	}
	
}
