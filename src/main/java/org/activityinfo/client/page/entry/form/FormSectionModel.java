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

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Component;

public class FormSectionModel<M> extends BaseModelData {

    private FormSection<M> section;

    public FormSectionModel() {
    }

    public static <M> FormSectionModel<M> forComponent(FormSection<M> section) {
        FormSectionModel<M> model = new FormSectionModel<M>();
        model.section = section;
        return model;
    }

    public FormSectionModel<M> withHeader(String header) {
        set("header", header);
        return this;
    }

    public FormSectionModel<M> withDescription(String description) {
        set("description", description);
        return this;
    }

    public Component getComponent() {
        return section.asComponent();
    }

    public FormSection<M> getSection() {
        return section;
    }

}
