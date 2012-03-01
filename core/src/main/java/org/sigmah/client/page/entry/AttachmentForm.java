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
		
		FileUploadField attachmentFiled = new FileUploadField();
		attachmentFiled.setFieldLabel(I18N.CONSTANTS.attachFile());
		attachmentFiled.setAllowBlank(false);
		attachmentFiled.setName("attachmentFile");
		binding.addFieldBinding(new FieldBinding(attachmentFiled, "attachmentFile"));
		add(attachmentFiled);
	}
	
	public FormBinding getBinding() {
        return binding;
    }
}
