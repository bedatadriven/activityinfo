package org.sigmah.client.page.entry;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

public class AttachmentForm extends FormPanel {

	private FormBinding binding;
	
	public AttachmentForm(){
		binding = new FormBinding(this);
		
		FileUploadField attachmentFile = new FileUploadField();
		attachmentFile.setFieldLabel(I18N.CONSTANTS.attachFile());
		attachmentFile.setAllowBlank(false);
		binding.addFieldBinding(new FieldBinding(attachmentFile, "attachmentFile"));
		add(attachmentFile);
	}
	
	public FormBinding getBinding() {
        return binding;
    }
}
