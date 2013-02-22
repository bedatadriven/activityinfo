package org.activityinfo.client.page.entry;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

public class AttachmentForm extends FormPanel {

    private FormBinding binding;

    public AttachmentForm() {
        binding = new FormBinding(this);

        FileUploadField attachmentFiled = new FileUploadField();
        attachmentFiled.setFieldLabel(I18N.CONSTANTS.attachFile());
        attachmentFiled.setAllowBlank(false);
        attachmentFiled.setName("attachmentFile");
        binding.addFieldBinding(new FieldBinding(attachmentFiled,
            "attachmentFile"));
        add(attachmentFiled);
    }

    public FormBinding getBinding() {
        return binding;
    }
}
