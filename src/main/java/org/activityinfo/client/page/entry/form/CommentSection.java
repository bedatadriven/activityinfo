package org.activityinfo.client.page.entry.form;

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
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextArea;

public class CommentSection extends FormSectionWithFormLayout<SiteDTO> {

    private final TextArea commentField;

    public CommentSection(int width, int height) {
        getFormLayout().setLabelAlign(LabelAlign.TOP);

        commentField = new TextArea();
        commentField.setName("comments");
        commentField.setFieldLabel(I18N.CONSTANTS.comments());
        if (width > 0) {
            getFormLayout().setDefaultWidth(width);
            commentField.setWidth(width);
        }
        if (height > 0) {
            commentField.setHeight(height);
        }
        add(commentField);
    }

    public CommentSection() {
        this(350, 0);
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
