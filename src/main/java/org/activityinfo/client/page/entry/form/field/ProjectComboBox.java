package org.activityinfo.client.page.entry.form.field;

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
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.ProjectDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ProjectComboBox extends ComboBox<ProjectDTO> {

    public ProjectComboBox(ActivityDTO activity) {

        ListStore<ProjectDTO> store = new ListStore<ProjectDTO>();
        store.add(activity.getDatabase().getProjects());

        setName("project");
        setDisplayField("name");
        setEditable(false);
        setStore(store);
        setTriggerAction(ComboBox.TriggerAction.ALL);
        setFieldLabel(I18N.CONSTANTS.project());
        setForceSelection(true);
        setAllowBlank(true);
    }
}
