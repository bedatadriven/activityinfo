package org.activityinfo.client.page.config.design;

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

import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.event.BindingEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

/**
 * Base class for design forms
 * 
 * @author Alex Bertram
 */
abstract class AbstractDesignForm extends FormPanel {
    public abstract FormBinding getBinding();

    public int getPreferredDialogWidth() {
        return 450;
    }

    public int getPreferredDialogHeight() {
        return 340;
    }

    public void hideFieldWhenNull(final Field<?> field) {
        getBinding().addListener(Events.Bind, new Listener<BindingEvent>() {

            @Override
            public void handleEvent(BindingEvent be) {
                field.setVisible(field.getValue() != null);
            }
        });
    }
}