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

import java.util.Collections;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.PartnerDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class PartnerComboBox extends ComboBox<PartnerDTO> {

    public PartnerComboBox(ActivityDTO activity) {
        this(allowablePartners(activity));
    }

    private static List<PartnerDTO> allowablePartners(ActivityDTO activity) {
        if (activity.getDatabase().isEditAllAllowed()) {
            return activity.getDatabase().getPartners();
        } else {
            return Collections.singletonList(activity.getDatabase()
                .getMyPartner());
        }
    }

    public PartnerComboBox(List<PartnerDTO> partners) {

        final ListStore<PartnerDTO> store = new ListStore<PartnerDTO>();
        store.add(partners);

        setName("partner");
        setDisplayField("name");
        setEditable(false);
        setTriggerAction(ComboBox.TriggerAction.ALL);
        setStore(store);
        setFieldLabel(I18N.CONSTANTS.partner());
        setForceSelection(true);
        setAllowBlank(false);

        if (store.getCount() == 1) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    setValue(store.getAt(0));
                }
            });
        }
    }

}
